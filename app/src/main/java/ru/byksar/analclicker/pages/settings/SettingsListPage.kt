package ru.byksar.analclicker.pages.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.alorma.compose.settings.ui.SettingsSwitch
import ru.byksar.analclicker.ui.theme.AnalClickerTheme

@Composable
fun SettingsListPage(mainNavController: NavController) {

    AnalClickerTheme {
        Scaffold (
            topBar = { TopBarSettings(mainNavController) },
            modifier = Modifier.fillMaxSize()
        ) {
                innerPadding ->
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                SettingsSwitch(
                    state = true,
                    title = { Text("Анимации") },
                    subtitle = { Text("Включить/Выключить анимции") },
                    onCheckedChange = {}
                )
                SettingsSwitch(
                    state = true,
                    title = { Text("Анимации") },
                    subtitle = { Text("Включить/Выключить анимции") },
                    onCheckedChange = {}
                )
                SettingsSwitch(
                    state = true,
                    title = { Text("Анимации") },
                    subtitle = { Text("Включить/Выключить анимции") },
                    onCheckedChange = {}
                )
                SettingsSwitch(
                    state = true,
                    title = { Text("Анимации") },
                    subtitle = { Text("Включить/Выключить анимции") },
                    onCheckedChange = {}
                )
            }
        }
    }





}
