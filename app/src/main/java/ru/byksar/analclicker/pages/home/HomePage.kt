package ru.byksar.analclicker.pages.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.byksar.analclicker.ClickerViewModel
import ru.byksar.analclicker.R

@Composable
fun HomePage(modifier: Modifier = Modifier, clickerViewModel: ClickerViewModel = viewModel()) {

    val count = clickerViewModel.count

    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.9f else 1.0f )
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    data class SpawnedText(val text: String, val offset: Offset)
    val spawns = remember { mutableStateListOf<SpawnedText>() }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(

            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }

                .shadow(10.dp, RectangleShape)
                .clip(RoundedCornerShape(10.dp))
                .indication(interactionSource, ripple())
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { pos ->
                            // Нажали — ужимаем
                            val press = PressInteraction.Press(pos)
                            interactionSource.emit(press)
                            isPressed = true

                            val succes = tryAwaitRelease()

                            if (succes) {interactionSource.emit(PressInteraction.Release(press))}
                            else {interactionSource.emit(PressInteraction.Cancel(press))}
                            isPressed = false
                        },
                        onTap = {
                            clickerViewModel.onCountClick()
                        })},



                        painter = painterResource(R.drawable.lev),
                        contentDescription = "Image",
                    )
                }
                spawns.forEach { spawn ->
                    // Переводим пиксели в dp
                    val xDp = with(density) { spawn.offset.x.toDp() }
                    val yDp = with(density) { spawn.offset.y.toDp() }

                    Text(
                        text = spawn.text,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .absoluteOffset(x = xDp, y = yDp)
                    )
                }
    }