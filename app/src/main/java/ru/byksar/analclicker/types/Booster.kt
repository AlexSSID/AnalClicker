package ru.byksar.analclicker.types

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import kotlin.math.pow

sealed interface BoosterType {
    object Passive: BoosterType
    object onTap: BoosterType
}

sealed class Booster(
    val id: String,
    val name: String,
    val baseCost: Long,
    val incomePerLevel: Long,
    val type: BoosterType = BoosterType.Passive,
    val level: MutableState<Int> = mutableIntStateOf(0)
) {
    val nextLevelCost: Long
        get() = (baseCost * 1.15.pow(level.value)).toLong()


    object AnalBooster : Booster(
        id = "passive_anal",
        name = "Anal",
        baseCost = 100,
        incomePerLevel = 1
    )

    object FingerBooster : Booster(
        id = "passive_finger",
        name = "Finger",
        baseCost = 500,
        incomePerLevel = 5
    )

    object Masturbirator : Booster(
        id = "passive_masturbation",
        name = "Masturbation",
        baseCost = 1000000,
        incomePerLevel = 1,
        type = BoosterType.onTap
    )


}

data class MainClicker(
    val name: String = "Masturbirator",
    val level: MutableState<Int> = mutableIntStateOf(0)
)