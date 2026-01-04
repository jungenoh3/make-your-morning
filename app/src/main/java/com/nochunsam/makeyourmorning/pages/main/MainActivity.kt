package com.nochunsam.makeyourmorning.pages.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.nochunsam.makeyourmorning.common.data.BlockTime
import com.nochunsam.makeyourmorning.common.data.DayCount
import com.nochunsam.makeyourmorning.pages.main.compose.NavigationGraph
import com.nochunsam.makeyourmorning.ui.theme.MakeYourMorningTheme
import com.nochunsam.makeyourmorning.utilities.block.FocusBlockingManager
import com.nochunsam.makeyourmorning.utilities.alarm.AlarmScheduler
import com.nochunsam.makeyourmorning.utilities.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private var userLeaving = false
    private var backPressed = false

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        userLeaving = true
    }

    override fun onStop() {
        val context = this
        super.onStop()

        when {
            backPressed -> {
                FocusBlockingManager.currentBlockId?.let { AlarmScheduler(context).cancel(it) }
            }
            userLeaving -> {
                FocusBlockingManager.currentBlockId?.let { AlarmScheduler(context).cancel(it) }
            }
        }
        backPressed = false
        userLeaving = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleShortcutIntent(intent)

        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.getInstance(this@MainActivity)?.dayCountDao()
            if (dao?.get() == null) {
                dao?.insert(DayCount(id = 1, count = 0))
            }
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
                    NavigationGraph(startDestination = "main")
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