package ru.byksar.analclicker.pages.settings
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.BuildCompat
import androidx.navigation.NavController
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.valentinilk.shimmer.shimmer
import ru.byksar.analclicker.BuildConfig
import ru.byksar.analclicker.UpgradeState

@Composable
fun SettingsPage(navController: NavController, modifier: Modifier = Modifier, settingsViewModel: SettingsViewModel) {

    LaunchedEffect(Unit) {if (settingsViewModel.getVersionState !is GetVersionState.Succes) settingsViewModel.fetchVersion()}



    val state = settingsViewModel.getVersionState
    var openAlertDialog = remember { mutableStateOf(false) }
    var openToast = remember { mutableStateOf(false) }


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
            enabled = state is GetVersionState.Succes,
            modifier = Modifier,
            icon = { Icon(
                Icons.Default.ArrowForwardIos, contentDescription = null)},
            action = {},
            onClick = {
                if (settingsViewModel.IsUpdateAvalilabe(BuildConfig.VERSION_NAME, (state as GetVersionState.Succes).version.tag_name))
                openAlertDialog.value = !openAlertDialog.value
                else openToast.value = true



            }
        )
        OpenCloseDialog(openAlertDialog, state)
        FastToast(openToast)

    }


}
@Composable
fun FastToast(openToast: MutableState<Boolean>) {
    when(openToast.value) {
        true -> {
            Toast.makeText(LocalContext.current, "Обновлений не найдено!", Toast.LENGTH_SHORT).show()
            openToast.value = false

        }
        false -> {}
    }
}

@Composable
fun OpenCloseDialog(openAlertDialog: MutableState<Boolean>, state: GetVersionState) {

    when (openAlertDialog.value) {
        true -> {
            AlertDialogUpdate(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = { },
                state = state)

        }
        false -> {}
    }

}



@Composable
fun AlertDialogUpdate(
    state: GetVersionState,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String = "Update Available",
    dialogText: String = "Найдено обновление\nТекущая версия: ${BuildConfig.VERSION_NAME}",
    icon: ImageVector = Icons.Default.Download,
) {
    AlertDialog(

        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth(),
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(65.dp), contentAlignment = Alignment.Center)
            {
                ElevatedCard(
                    modifier = Modifier
                        .height(65.dp)
                        .fillMaxWidth(0.5f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)

                ) { Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize()) {
                    when(state) {
                        GetVersionState.Error -> Text("Error")
                        GetVersionState.Loading -> Text("Loading", modifier = Modifier.shimmer())
                        is GetVersionState.Succes -> Text(fontSize = 24.sp,text = "v ${state.version.tag_name}")
                    }
                }
                }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}