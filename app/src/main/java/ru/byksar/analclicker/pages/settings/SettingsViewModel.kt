package ru.byksar.analclicker.pages.settings

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.github.g00fy2.versioncompare.Version
import kotlinx.coroutines.launch
import ru.byksar.analclicker.simpleapi.ApiClient

sealed interface GetVersionState {
    object Loading: GetVersionState
    object Error: GetVersionState
    data class Succes(var version: VersionModel): GetVersionState
}

class SettingsViewModel(application: Application ) : AndroidViewModel(application) {

    val apiClient: ApiClient = ApiClient()
    var getVersionState by mutableStateOf<GetVersionState>(GetVersionState.Loading)




    fun fetchVersion() {
        getVersionState = GetVersionState.Loading

        viewModelScope.launch {
            val version = apiClient.getAppVersion()

            if (version is VersionModel) {
                getVersionState = GetVersionState.Succes(version)
                Log.i("SettingsViewModel", version.toString())
            }
            else {
                Log.i("SettingsViewModel", version.toString())
                getVersionState = GetVersionState.Error
            }



        }

    }

    fun IsUpdateAvalilabe(currentVersion: String, otherVersion: String): Boolean {
        return Version(currentVersion) < Version(otherVersion)
    }



}