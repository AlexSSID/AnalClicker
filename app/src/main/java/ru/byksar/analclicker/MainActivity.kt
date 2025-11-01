package ru.byksar.analclicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.byksar.analclicker.pages.settings.SettingsListPage


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()



        setContent {
            val mainNavController = rememberNavController()

            NavHost(
                navController = mainNavController,
                startDestination = "MainHome",
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                composable(route = "MainHome",) { MainApp(mainNavController)  }
                composable(
                    route = "SettingsListPage",
                    enterTransition = {slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)},
                    exitTransition = {slideOutOfContainer(animationSpec = tween(300, easing = EaseOut), towards = AnimatedContentTransitionScope.SlideDirection.End) }) { SettingsListPage(mainNavController) }
            }
        }



    }
}





