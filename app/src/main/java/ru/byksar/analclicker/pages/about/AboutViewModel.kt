package ru.byksar.analclicker.pages.about

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.byksar.analclicker.simpleapi.ApiClient
import ru.byksar.analclicker.simpleapi.JokeModel

sealed interface JokeUiState {
    data class Success(val jokeText: String) : JokeUiState
    data class Error(val error: String) : JokeUiState
    object Loading : JokeUiState
    data class Translated(val string: String): JokeUiState
}

class AboutViewModel: ViewModel() {

    private val apiClient = ApiClient()
    var jokeUiState: JokeUiState by mutableStateOf(JokeUiState.Loading)

    val joke = mutableStateOf<JokeModel?>(null)

    fun fetchJoke() {
        jokeUiState = JokeUiState.Loading
        viewModelScope.launch {

            val jokeModel = apiClient.getJoke()

            if (jokeModel?.joke != "") {
                jokeUiState = JokeUiState.Success(jokeModel?.joke ?: "what")
            }


        }

    }

    fun fetchTranslate(text: String) {
        jokeUiState = JokeUiState.Loading
        viewModelScope.launch {
            val joke_translated = apiClient.getTranslate(text)

            jokeUiState = JokeUiState.Translated(joke_translated?: "Ошибка")


        }
    }
}