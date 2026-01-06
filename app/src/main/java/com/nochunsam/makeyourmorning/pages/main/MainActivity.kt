package com.nochunsam.makeyourmorning.pages.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.nochunsam.makeyourmorning.common.compose.NavigationGraph
import com.nochunsam.makeyourmorning.common.data.BlockTime
import com.nochunsam.makeyourmorning.ui.theme.MakeYourMorningTheme
import com.nochunsam.makeyourmorning.utilities.alarm.AlarmScheduler
import com.nochunsam.makeyourmorning.utilities.block.FocusBlockingManager
import com.nochunsam.makeyourmorning.utilities.splash.SplashViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var userLeaving = false
    private var backPressed = false
    private val viewModel: SplashViewModel by viewModels()

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        userLeaving = true
    }

    override fun onStop() {
        val context = this
        if (backPressed || userLeaving){
            FocusBlockingManager.currentBlockId?.let { AlarmScheduler(context).cancel(it) }
            FocusBlockingManager.stopBlocking()
        }
        backPressed = false
        userLeaving = false
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        handleShortcutIntent(intent)

        splashScreen.setKeepOnScreenCondition {
            viewModel.isLoading.value
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backPressed = true
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        )

        lifecycleScope.launch {
            setContent {
                MakeYourMorningTheme {
                    val startDestination by viewModel.startDestination.collectAsState()
                    // 목적지가 결정됐을 때 graph 생성
                    if (startDestination != null) {
                        println("최종 startDestination: $startDestination")
                        NavigationGraph(
                            startDestination = startDestination!!,
                            setFirstOpen = { viewModel.setIntroFinished() }
                        )
                    }
                }
            }
        }
    }

    private fun handleShortcutIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_VIEW) {
            val minute = intent.getIntExtra("minute", 0)
            val blockTime = BlockTime(minute = minute)
            AlarmScheduler(this).scheduleBlock(blockTime)
            FocusBlockingManager.startBlockingFor(
                blockId = blockTime.id,
                minutes = blockTime.minute
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleShortcutIntent(intent)
    }

}