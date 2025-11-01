package ru.byksar.analclicker.pages.settings

import android.os.Build
import kotlinx.serialization.Serializable

@Serializable
data class VersionModel(
    val currentVersion: String = Build.VERSION.RELEASE,
    val tag_name: String,
    val assets: List<AssetModel>
)

@Serializable
data class AssetModel(
    val browser_download_url: String
)