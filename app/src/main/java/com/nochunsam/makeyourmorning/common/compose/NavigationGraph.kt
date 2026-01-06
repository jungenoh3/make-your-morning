package com.nochunsam.makeyourmorning.common.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.nochunsam.makeyourmorning.pages.intro.screen.Intro
import com.nochunsam.makeyourmorning.pages.main.screen.MainScreen
import com.nochunsam.makeyourmorning.pages.setting.screen.Setting
import com.nochunsam.makeyourmorning.pages.setting.screen.Tutorial

@Composable
fun NavigationGraph(
    startDestination: String = "main",
    setFirstOpen: () -> Unit
    ){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = "intro") {
            Intro(navigateToMain = {
                setFirstOpen()

                navController.navigate("main") {
                    popUpTo("main") {inclusive = true}
                }
            })
        }

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
                    onNavigateToLogin = {

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