package ru.byksar.analclicker.pages.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun AboutPage(modifier: Modifier = Modifier, viewModel: AboutViewModel) {

    val summarySpacing = 16.dp
    var jokeTempText by remember {mutableStateOf<String?>(null)}

    LaunchedEffect(Unit) {
        if ((viewModel.jokeUiState !is JokeUiState.Success) && (viewModel.jokeUiState !is JokeUiState.Translated)) {
            viewModel.fetchJoke()
        }
    }

    Column(
        modifier = Modifier
            .padding(summarySpacing)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when(val state = viewModel.jokeUiState) {
            is JokeUiState.Error -> JokeCard("Error")
            is JokeUiState.Loading -> {
                JokeCard(text = jokeTempText?: "", modifier = modifier
                    .shimmer())}
            is JokeUiState.Success -> {
                JokeCard(text = state.jokeText)
                jokeTempText = state.jokeText}
            is JokeUiState.Translated -> {
                JokeCard(text = state.string)
                jokeTempText = state.string }
        }

        Spacer(modifier.height(20.dp))
        FilledIconButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {viewModel.fetchJoke()}
        ) {
            Text("ahahaha, new")
        }
    }
}

@Composable
fun JokeCard(text: String = "", modifier: Modifier = Modifier) {
    Card(
        modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            Text(text)

        }
    }
}