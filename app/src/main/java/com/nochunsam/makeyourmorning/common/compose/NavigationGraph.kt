package com.nochunsam.makeyourmorning.common.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.nochunsam.makeyourmorning.pages.intro.screen.Intro
import com.nochunsam.makeyourmorning.pages.main.screen.MainScreen
import com.nochunsam.makeyourmorning.pages.setting.screen.EmailLogin
import com.nochunsam.makeyourmorning.pages.setting.screen.LoginOption
import com.nochunsam.makeyourmorning.pages.setting.screen.Setting
import com.nochunsam.makeyourmorning.pages.setting.screen.Signup
import com.nochunsam.makeyourmorning.pages.setting.screen.Tutorial
import com.nochunsam.makeyourmorning.utilities.user.FirebaseViewModel

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
            MainScreen (
                onNavigateToSettings = {
                    navController.navigate("settings_graph")
                }
            )
        }
        navigation (startDestination = "setting", route = "settings_graph") {
            val firebaseViewModel = FirebaseViewModel()

            composable(route = "setting") {
                Setting(
                    viewModel = firebaseViewModel,
                    onNavigateToTutorial = {
                        navController.navigate("tutorial")
                    },
                    onNavigateToLogin = {
                        navController.navigate("authentication")
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

            navigation (startDestination = "login_options", route = "authentication") {
                composable(route = "login_options") {
                    LoginOption(
                        onBack = {
                            navController.popBackStack()
                        },
                        onNavigateToEmailLogin = {
                            navController.navigate("email_login")
                        },
                        onGoogleLoginClick = {},
                    )
                }
                composable (route = "email_login") {
                    EmailLogin(
                        viewModel = firebaseViewModel,
                        onLoginSuccess = {
                            navController.navigate("setting") {
                                popUpTo("setting") { inclusive = true }
                            }
                        },
                        onNavigateToSignup = {
                            navController.navigate("signup")
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
                composable (route = "signup") {
                    Signup(
                        viewModel = firebaseViewModel,
                        onSignupSuccess = {
                            navController.popBackStack()
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }

        }
    }
}