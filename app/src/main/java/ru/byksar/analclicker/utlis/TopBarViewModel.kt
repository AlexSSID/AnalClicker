package ru.byksar.analclicker.utlis

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel

class TopBarViewModel(application: Application ) : AndroidViewModel(application) {
    val progress = mutableFloatStateOf(0f)

}