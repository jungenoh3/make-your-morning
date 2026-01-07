package com.nochunsam.makeyourmorning.common.compose

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.nochunsam.makeyourmorning.pages.intro.screen.IntroScreen
import com.nochunsam.makeyourmorning.pages.main.screen.MainScreen
import com.nochunsam.makeyourmorning.pages.setting.screen.EmailLoginScreen
import com.nochunsam.makeyourmorning.pages.setting.screen.LoginOptionScreen
import com.nochunsam.makeyourmorning.pages.setting.screen.SettingScreen
import com.nochunsam.makeyourmorning.pages.setting.screen.SignupScreen
import com.nochunsam.makeyourmorning.pages.setting.screen.TutorialScreen
import com.nochunsam.makeyourmorning.utilities.database.DatabaseViewModel
import com.nochunsam.makeyourmorning.utilities.user.FirebaseViewModel

@Composable
fun NavigationGraph(
    startDestination: String = "main",
    setFirstOpen: () -> Unit
    ){
    val navController = rememberNavController()
    val context = LocalContext.current
    val databaseViewModel = DatabaseViewModel(context.applicationContext as Application)

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = "intro") {
            IntroScreen(navigateToMain = {
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
                SettingScreen(
                    firebaseViewModel = firebaseViewModel,
                    databaseViewModel = databaseViewModel,
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
                TutorialScreen(onBack = {
                    navController.popBackStack()
                })
            }

            navigation (startDestination = "login_options", route = "authentication") {
                composable(route = "login_options") {
                    LoginOptionScreen(
                        onBack = {
                            navController.popBackStack()
                        },
                        onNavigateToEmailLogin = {
                            navController.navigate("email_login")
                        },
                        onGoogleLoginClick = {
                            firebaseViewModel.loginInWithGoogle(
                                context = context,
                                onSuccess = {
                                    navController.popBackStack()
                                }
                            )
                        },
                    )
                }
                composable (route = "email_login") {
                    EmailLoginScreen(
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
                    SignupScreen(
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