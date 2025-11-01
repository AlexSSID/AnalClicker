package ru.byksar.analclicker.utlis

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.FileProvider
import java.io.File

fun installApk(context: Context, downloadId: Long) {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    // 1. Получаем путь к файлу из DownloadManager
    val query = DownloadManager.Query().setFilterById(downloadId)
    val cursor = downloadManager.query(query)

    if (cursor.moveToFirst()) {
        val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
        val status = cursor.getInt(columnIndex)

        if (status == DownloadManager.STATUS_SUCCESSFUL) {
            // Получаем URI файла из DownloadManager (это внутренний file:// URI)
            val pathIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
            val fileUri = Uri.parse(cursor.getString(pathIndex))

            // 2. Преобразуем file:// URI в content:// URI с помощью FileProvider
            val file = File(fileUri.path ?: return) // Получаем File объект из URI

            // Если вы использовали setDestinationInExternalPublicDir,
            // вам может понадобиться получить путь более явно или использовать
            // getUriForDownloadedFile, который может вернуть content:// URI

            // Пример использования FileProvider для преобразования
            val contentUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider", // Должно совпадать с authorities в Manifest
                file
            )

            // 3. Запускаем установщик пакетов
            val installIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(contentUri, "application/vnd.android.package-archive")
                // Важно: дать временное разрешение установщику пакетов
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                // Важно: запуск из BroadcastReceiver или фонового сервиса требует NEW_TASK
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            // Проверка разрешения REQUEST_INSTALL_PACKAGES (для Android O+)
            if (context.packageManager.canRequestPackageInstalls()) {
                context.startActivity(installIntent)
            } else {
                // Запрос на разрешение "Установка неизвестных приложений"
                val settingsIntent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                    data = Uri.parse("package:${context.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(settingsIntent)
                // После получения разрешения пользователь должен будет вернуться в ваше приложение
                // и повторить попытку установки (или вы должны отследить это через onActivityResult/registerForActivityResult)
            }
        }
    }
    cursor.close()
}