package com.nochunsam.makeyourmorning.common.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.nochunsam.makeyourmorning.pages.main.screen.MainScreen
import com.nochunsam.makeyourmorning.pages.setting.screen.Setting
import com.nochunsam.makeyourmorning.pages.setting.screen.Tutorial

@Composable
fun NavigationGraph(startDestination: String = "main"){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route =  "main") {
            MainScreen(
                onNavigateToSettings = {
                    navController.navigate("settings_graph")
                }
            )
        }
        navigation (startDestination = "setting", route = "settings_graph") {
            composable(route = "setting") {
                Setting(
                    onNavigateToTutorial = {
                        navController.navigate("tutorial")
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(route = "tutorial") {
                Tutorial(onBack = {
                    navController.popBackStack()
                })
            }

        }


    }
}