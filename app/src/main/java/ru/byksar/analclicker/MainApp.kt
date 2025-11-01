package ru.byksar.analclicker

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.GTranslate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.byksar.analclicker.pages.about.AboutPage
import ru.byksar.analclicker.pages.about.AboutViewModel
import ru.byksar.analclicker.pages.about.JokeUiState
import ru.byksar.analclicker.pages.boosters.BoostersPage
import ru.byksar.analclicker.pages.home.HomePage
import ru.byksar.analclicker.pages.settings.SettingsListPage
import ru.byksar.analclicker.pages.settings.SettingsPage
import ru.byksar.analclicker.pages.settings.SettingsViewModel
import ru.byksar.analclicker.pages.settings.TopBarSettings
import ru.byksar.analclicker.ui.theme.AnalClickerTheme
import ru.byksar.analclicker.utlis.TopBarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(mainNavController: NavController) {
    val homeTab = TabBarItem(title = "Home", unselectedIcon = Icons.Outlined.Home, selectedIcon = Icons.Default.Home)
    val boosterTab = TabBarItem(title = "Boosters", unselectedIcon = Icons.Outlined.Build, selectedIcon = Icons.Default.Build)
    val aboutTab = TabBarItem(title = "About", unselectedIcon = Icons.Outlined.Info, selectedIcon = Icons.Default.Info )
    val settingsTab = TabBarItem(title = "Settings", unselectedIcon = Icons.Outlined.Settings, selectedIcon = Icons.Default.Settings )
    val tabItemsList = listOf(homeTab, boosterTab, aboutTab, settingsTab)

    val clickerViewModel: ClickerViewModel = viewModel()
    val aboutViewModel: AboutViewModel = viewModel()
    val topBarViewModel: TopBarViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val topBarExists = remember { mutableStateOf<Boolean>(false) }
    val bottomBar = @Composable { TabView(tabItems = tabItemsList, navController = navController) }

    AnalClickerTheme {
        Scaffold (
            topBar = {TopBar(clickerViewModel, navController, topBarExists, topBarViewModel)},
            floatingActionButton = { FloatingButton(clickerViewModel = clickerViewModel, aboutViewModel = aboutViewModel, navController =  navController, onClick = {showBottomSheet = true}) },
            modifier = Modifier.fillMaxSize(),
            bottomBar = bottomBar
        ) {innerPadding ->

            NavHost(
                navController = navController,
                startDestination = homeTab.title,
                modifier = Modifier.padding(innerPadding),


                enterTransition = { slideInVertically(        spring(
                    stiffness = 3300f,
                    visibilityThreshold = IntOffset.VisibilityThreshold,
                )) + fadeIn(tween(250)) },

                exitTransition =  { slideOutVertically() + fadeOut(tween(50)) }

            ) {
                composable(homeTab.title) { HomePage(clickerViewModel = clickerViewModel) }
                composable(boosterTab.title) { BoostersPage(clickerViewModel = clickerViewModel, topBarViewModel = topBarViewModel) }
                composable(aboutTab.title) { AboutPage(viewModel = aboutViewModel) }
                composable(settingsTab.title) {
                    SettingsPage(mainNavController, settingsViewModel = settingsViewModel )
                }
                composable(
                    route = "SettingsListPage",
                    enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) { it } },
                    exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) { it } }
                ) { SettingsListPage(mainNavController) }
            }
        }
    }
}
@Composable
fun SelectBottomBar(navController: NavController) {
    val backStackEntry = navController.currentBackStackEntryAsState()


}

@Composable
fun TopBar(clickerViewModel: ClickerViewModel, navController: NavController, topBarExists: MutableState<Boolean>, topBarViewModel: TopBarViewModel) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val zapretRoutes = listOf("SettingsListPage")

    if (!zapretRoutes.contains(currentRoute)) {
        TopBarElement(clickerViewModel = clickerViewModel, topBarViewModel = topBarViewModel)

    }

    if(zapretRoutes.contains(currentRoute)) {
        TopBarSettings(navController)

    }



}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedCounter(count: String,modifier: Modifier = Modifier) {
    var oldcount by remember { mutableStateOf(count) }

    SideEffect {
        oldcount = count
    }

    val countString = count
    val oldCountString = oldcount

    Row(modifier = modifier) {
        for (i in countString.indices) {
            val oldChar = oldCountString.getOrNull(i)
            val newChar = countString[i]

            val char = if (oldChar == newChar) {
                oldCountString[i]
            } else {
                countString[i]
            }

            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically { it } with slideOutVertically { -it }
                }
            ) { char ->
                Text(text = char.toString(), softWrap = false, fontSize = 24.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun TopBarElement(clickerViewModel: ClickerViewModel, collapsed: Dp = 100.dp, expanded: Dp = 280.dp, topBarViewModel: TopBarViewModel) {

    val density = LocalDensity.current
    val rangePx = with(density) { (expanded - collapsed).toPx() }
    var offsetPx by remember { mutableStateOf(0f) }
    val progress = (offsetPx / rangePx).coerceIn(0f, 1f)
    val count = clickerViewModel.count
    topBarViewModel.progress.value = progress

    val height by animateDpAsState(
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        targetValue = collapsed + (expanded - collapsed) * progress,
        label = "height"
    )

    val dragState = rememberDraggableState { delta ->
        offsetPx = (offsetPx + delta).coerceIn(0f, rangePx)
    }

    val velocityThreshold = with(density) { 400.dp.toPx() }

    fun settle(velocity: Float) {
        offsetPx = when {

            velocity > velocityThreshold -> rangePx

            velocity < -velocityThreshold -> 0f

            progress < 0.5f -> 0f
            else -> rangePx
        }
    }

    var isLoaded by remember { mutableStateOf(false) }

    val animatedColor by animateColorAsState(
        targetValue = when(val state = clickerViewModel.textState) {
            UpgradeState.Error -> Color.Red
            UpgradeState.Idle -> Color.Unspecified.takeOrElse { LocalTextStyle.current.color.takeOrElse { LocalContentColor.current } }
            UpgradeState.Succes -> Color.Green
        },
        label = "errorColor",
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)
    )


    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(bottomEnd = 40.dp, bottomStart = 40.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .draggable(
                state = dragState,
                orientation = Orientation.Vertical,
                onDragStopped = { velocity -> settle(velocity) }
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(blue = 0.5f)),
    )
    {
        Box(modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()) {
            Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

                Column(modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)) {
                    AnimatedVisibility(visible = progress > 0.05f) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .graphicsLayer { alpha = progress } // плавное появление
                                .padding(top = 12.dp)
                                .fillMaxSize()
                        ) {
                            Text("Доп. контент", fontSize = 18.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("я не ебу даже как эта хуйня работает.")
                            Text(clickerViewModel.countAvg.toString() + "/с")
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f))
                {

                    AnimatedCounter(count.value.toString())
                }
            }
        }
    }

}



@Composable
fun FloatingButton(clickerViewModel: ClickerViewModel, aboutViewModel: AboutViewModel, navController: NavController, onClick: () -> Unit) {

val navBackStackEntry by navController.currentBackStackEntryAsState()
val currentRoute = navBackStackEntry?.destination?.route;



if (currentRoute == "Home") {
    FloatingActionButton(
        onClick = {
            clickerViewModel.onCountClick(100)
            onClick()

        },
    ) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}
if (currentRoute == "About") {
    FloatingActionButton(
        onClick = {
            if (aboutViewModel.jokeUiState is JokeUiState.Success) {
                val joke = (aboutViewModel.jokeUiState as JokeUiState.Success).jokeText

                if (aboutViewModel.jokeUiState !is JokeUiState.Translated) {
                    aboutViewModel.fetchTranslate(joke)
                }


            }

        },
    ) {
        Icon(Icons.Filled.GTranslate, "Floating action button.")
    }

}

}