package ru.byksar.analclicker

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.byksar.analclicker.pages.settings.SettingsListPage
import ru.byksar.analclicker.utlis.MainInitialize


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()

        val mainService = this
        MainInitialize(this)






        setContent {
            val mainNavController = rememberNavController()

            NavHost(
                navController = mainNavController,
                startDestination = "MainHome",
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                composable(route = "MainHome",) { MainApp(mainNavController, mainService)  }
                composable(
                    route = "SettingsListPage",
                    enterTransition = {slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)},
                    exitTransition = {slideOutOfContainer(animationSpec = tween(300, easing = EaseOut), towards = AnimatedContentTransitionScope.SlideDirection.End) }) { SettingsListPage(mainNavController) }
            }
        }



    }
}







