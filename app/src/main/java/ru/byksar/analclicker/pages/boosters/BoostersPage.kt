package ru.byksar.analclicker.pages.boosters

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.byksar.analclicker.ClickerViewModel
import ru.byksar.analclicker.types.Booster
import ru.byksar.analclicker.types.MainClicker
import ru.byksar.analclicker.utlis.TopBarViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BoostersPage(modifier: Modifier = Modifier, clickerViewModel: ClickerViewModel, topBarViewModel: TopBarViewModel) {

    val summarySpacing = 16.dp

    val boosters = clickerViewModel.allBoosters
    val mainClicker by clickerViewModel.mainClicker.collectAsState()


    Column(
        modifier = modifier
            .padding(summarySpacing)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(summarySpacing)

    ) {

        BigEmpty(modifier = Modifier
            .fillMaxSize()
            .weight(3f), clickerViewModel = clickerViewModel)

        Row(
            horizontalArrangement = Arrangement.spacedBy(summarySpacing),
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )
        {
            BoosterCard(
                onUpgradeClicker = {
                    clickerViewModel.tryUpgradeBooster(boosters[0], clickerViewModel.count.value)
                },
                booster = boosters[0],
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f),
                clickerViewModel = clickerViewModel,
                topBarViewModel = topBarViewModel

            )
            BoosterCard(
                onUpgradeClicker = {
                    clickerViewModel.tryUpgradeBooster(boosters[1], clickerViewModel.count.value)
                },
                booster = boosters[1],
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f),
                clickerViewModel = clickerViewModel,
                topBarViewModel = topBarViewModel
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(summarySpacing),
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )
        {
            BoosterCard(
                onUpgradeClicker = {
                    clickerViewModel.tryUpgradeBooster(boosters[2], clickerViewModel.count.value)
                },
                booster = boosters[2],
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f),
                clickerViewModel = clickerViewModel,
                topBarViewModel = topBarViewModel
            )
        }

    }

    }



@Composable
fun BigEmpty(modifier: Modifier = Modifier, clickerViewModel: ClickerViewModel) {
    val interactionSource = remember { MutableInteractionSource() }
    val isError = clickerViewModel.isError

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(
                    text = "Masturbirator", 
                    fontSize = 36.sp,
                )
            }
        }
    }
}

@Composable
fun BoosterCard(modifier: Modifier = Modifier, booster: Booster, onUpgradeClicker:() -> Unit, clickerViewModel: ClickerViewModel, topBarViewModel: TopBarViewModel) {

    val interactionSource = remember { MutableInteractionSource() }
    val count = clickerViewModel.count
    val progress = 1f - topBarViewModel.progress.value

    OutlinedCard(
        enabled = if (count.value >= booster.nextLevelCost) true else false,
        border = BorderStroke(1.dp, Color.Black),
        modifier = modifier,
        onClick = onUpgradeClicker,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        ) {

            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween ) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(booster.name, fontSize = 16.sp, modifier = Modifier.padding(5.dp))
                    Card(

                        shape = RoundedCornerShape(5.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary),
                    ) {Box(Modifier.padding(5.dp)) { Text("Level: ${booster.level.value}") } }
                }
                Row(modifier = Modifier.fillMaxWidth().graphicsLayer(alpha = progress)) {
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(5.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary),
                    ) {Box(Modifier.padding(5.dp)) { Text("Cost: ${booster.nextLevelCost}") } }
                }




            }



            }
        }
    }
