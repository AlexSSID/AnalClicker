package ru.byksar.analclicker.pages.settings

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import com.valentinilk.shimmer.shimmer

@Composable
fun SettingsPage(navController: NavController, modifier: Modifier = Modifier, settingsViewModel: SettingsViewModel) {

    LaunchedEffect(Unit) {if (settingsViewModel.getVersionState !is GetVersionState.Succes) settingsViewModel.fetchVersion()}

    val state = settingsViewModel.getVersionState


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        SettingsMenuLink(
            title = { Text(text = "Update") },
            subtitle = {
                when(state) {
                    GetVersionState.Error -> Text("Error")
                    GetVersionState.Loading -> Text("Loading...", modifier = Modifier.shimmer())
                    is GetVersionState.Succes -> Text(state.version.tag_name)
                }
            },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(
                Icons.Default.ArrowForwardIos, contentDescription = null)},
            action = {},
            onClick = {navController.navigate("SettingsListPage")},
        )
    }

}