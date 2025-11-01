package ru.byksar.analclicker.pages.settings
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.valentinilk.shimmer.shimmer
import ru.byksar.analclicker.BuildConfig
import androidx.core.net.toUri
import ru.byksar.analclicker.MainActivity
import java.io.File

@Composable
fun SettingsPage(
    navController: NavController,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
    mainService: MainActivity
) {

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
        OpenCloseDialog(openAlertDialog, state, mainService)
        FastToast(openToast)

    }


}


fun downloadLatestVersion(state: GetVersionState, mainService: MainActivity) {

    val requestCall = DownloadManager.Request((state as GetVersionState.Succes).version.assets.firstOrNull()?.browser_download_url?.toUri())
        .apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI and DownloadManager.Request.NETWORK_MOBILE)
            setTitle("Последняя версия (${state.version.tag_name})")
                .setDescription("Жди")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "AnalClicker-latest.apk")
        }
    val downloadManager = mainService.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val contentResolver = mainService.contentResolver
    val req_id = downloadManager.enqueue(requestCall)

    val query = DownloadManager.Query().setFilterById(req_id)

    val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)


            val cursor = downloadManager.query(query)

            if (cursor!= null && cursor.moveToFirst()) {
                val downloaded = cursor.getLong(cursor.getColumnIndexOrThrow(
                    DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR
                ))
                val total = cursor.getLong(cursor.getColumnIndexOrThrow(
                    DownloadManager.COLUMN_TOTAL_SIZE_BYTES
                ))

                val progress = (downloaded / total) * 100



            }
        }
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
fun OpenCloseDialog(openAlertDialog: MutableState<Boolean>, state: GetVersionState, mainService: MainActivity) {

    when (openAlertDialog.value) {
        true -> {
            AlertDialogUpdate(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    downloadLatestVersion(state = state, mainService)
                    openAlertDialog.value = false
                                 },
                state = state)

        }
        false -> {}
    }

}

