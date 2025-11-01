package ru.byksar.analclicker

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.byksar.analclicker.types.Booster
import ru.byksar.analclicker.types.BoosterType
import ru.byksar.analclicker.types.MainClicker
import kotlin.math.absoluteValue

sealed interface UpgradeState {
    object Error: UpgradeState
    object Succes : UpgradeState
    object Idle: UpgradeState
}

class ClickerViewModel(application: Application ) : AndroidViewModel(application) {


    val prefs = application.getSharedPreferences(
        "clicker_prefs",
        Context.MODE_PRIVATE
    )
    val COUNT_KEY = "click_count"
    var count = mutableLongStateOf(prefs.getLong(COUNT_KEY, 0))
    val analBooster = Booster.AnalBooster
    val fingerBooster = Booster.FingerBooster
    val masturbiratorBooster = Booster.Masturbirator

    val allBoosters = listOf(analBooster, fingerBooster, masturbiratorBooster)

    val mainClicker = MutableStateFlow(MainClicker())

    var textState by mutableStateOf<UpgradeState>(UpgradeState.Idle)

    var isError by mutableStateOf(false)
    var isOk by mutableStateOf(false)


    var countPerSecond by mutableStateOf<Long?>(null)
    var countAvg by mutableStateOf<Long?>(null)

    init {

        getPassiveIncomeInfinite()
        loadBoosterLevels()
        getAverageCount()

    }

    private fun loadBoosterLevels() {
        analBooster.level.value = prefs.getInt(analBooster.id, analBooster.level.value)
        fingerBooster.level.value = prefs.getInt(fingerBooster.id, fingerBooster.level.value)
    }


    fun getPassiveIncomeInfinite(delay_int: Long = 1000) {
        viewModelScope.launch {
            while (true) {
                delay(delay_int)
                updateCount(count.value + getTotalPassiveIncome())
            }
        }
    }

    fun getAverageCount() {
        viewModelScope.launch {
            while (true) {
                val tempValue = count.value
                delay(1000)
                val value = count.value - tempValue
                countAvg = value.absoluteValue
            }
        }

    }

    fun tryUpgradeBooster(booster: Booster, currentScore: Long): Boolean {
        val cost = booster.nextLevelCost
        if (currentScore >= cost) {
            booster.level.value++
            spendCount(cost)
            prefs.edit {
                putInt(booster.id, booster.level.value)
            }
            if (textState is UpgradeState.Idle) viewModelScope.launch {
                textState = UpgradeState.Succes
                delay(1000)
                textState = UpgradeState.Idle
            }
            return true

        }
        if (textState is UpgradeState.Idle) viewModelScope.launch {
            textState = UpgradeState.Error
            delay(1000)
            textState = UpgradeState.Idle
        }
        return false
    }
    fun getTotalPassiveIncome(): Long {
        // Считаем общий доход, суммируя доход от каждого бустера

        var total: Long = 0

        allBoosters.forEach { booster ->
            if (booster.type is BoosterType.Passive) total += (booster.level.value * booster.incomePerLevel )
        }

        return total
    }
    private fun updateCount(newCount: Long) {
        count.value = newCount //Logic
        countPerSecond = newCount + getTotalPassiveIncome() // Stats

        prefs.edit {
            putLong(COUNT_KEY, newCount)
        }


        checkAchievements(newCount)
    }
    private fun checkAchievements(currentCount: Long) {
        // Здесь будут проверки
    }
    fun onCountClick(extraLong: Long = 0) {
        var newCount = (count.value + 1) + extraLong // (+1 = tap on lev)
        allBoosters.forEach { booster ->  if (booster.type is BoosterType.onTap) newCount += (booster.level.value * booster.incomePerLevel) }

        updateCount(newCount)
    }
    fun spendCount(howmuch: Long) {
        count.value = count.value - howmuch
    }
}