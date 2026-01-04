package com.nochunsam.makeyourmorning.pages.main.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nochunsam.makeyourmorning.pages.main.screen.MainScreen
import com.nochunsam.makeyourmorning.pages.tutorial.screen.Tutorial
import com.nochunsam.makeyourmorning.pages.setting.screen.Setting

@Composable
fun NavigationGraph(startDestination: String = "main"){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route =  "main") {
            MainScreen(rootNavController = navController)
        }
        composable(route = "setting") {
            Setting(rootNavController = navController)
        }
        composable(route = "tutorial") {
            Tutorial(navController = navController)
        }
    }
}