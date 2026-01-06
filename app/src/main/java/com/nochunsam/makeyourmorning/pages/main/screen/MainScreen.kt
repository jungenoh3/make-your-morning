package com.nochunsam.makeyourmorning.pages.main.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nochunsam.makeyourmorning.common.compose.CustomScaffold
import com.nochunsam.makeyourmorning.pages.timer.screen.TimerScreen

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Timer : BottomNavItem("timer_screen", "Timer", Icons.Default.Home)
    object Record : BottomNavItem("record_screen", "Record", Icons.Default.List)
}

@Composable
fun MainScreen (
    onNavigateToSettings: () -> Unit // Timer에서 설정으로 갈 때 사용
) {
    val bottomNavController = rememberNavController()

    val items = listOf(
        BottomNavItem.Timer,
        BottomNavItem.Record
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                        label = { Text(text = item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            bottomNavController.navigate(item.route) {
                                // 1. 탭 클릭 시 백스택이 계속 쌓이는 것을 방지 (시작 지점까지 팝업)
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // 2. 같은 탭을 여러 번 눌렀을 때 화면이 재생성되는 것 방지
                                launchSingleTop = true
                                // 3. 이전에 선택된 상태 복원
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Timer.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Timer.route) {
                TimerScreen(onNavigateToSettings = onNavigateToSettings)
            }
            composable(BottomNavItem.Record.route) {
                RecordScreen()
            }
        }
    }
}

@Composable
fun RecordScreen() {
    CustomScaffold(
        title = "레코드",
        onBack = {}
    ) { }
}