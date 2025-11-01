package ru.byksar.analclicker.utlis

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import ru.byksar.analclicker.MainActivity

fun MainInitialize (activity: MainActivity) {
    val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)

            installApk(context, id)
            context.unregisterReceiver(this)

        }
    }
    val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
    activity.registerReceiver(onDownloadComplete, filter, RECEIVER_EXPORTED)

}