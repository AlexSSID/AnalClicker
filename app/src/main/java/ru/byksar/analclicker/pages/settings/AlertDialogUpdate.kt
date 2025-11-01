package ru.byksar.analclicker.pages.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valentinilk.shimmer.shimmer
import ru.byksar.analclicker.BuildConfig

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