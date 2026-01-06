package com.nochunsam.makeyourmorning.pages.timer.screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nochunsam.makeyourmorning.common.data.BlockTime
import com.nochunsam.makeyourmorning.pages.timer.compose.CircularTimerPicker
import com.nochunsam.makeyourmorning.pages.timer.compose.CountdownCircularTimer
import com.nochunsam.makeyourmorning.utilities.block.FocusBlockingManager
import com.nochunsam.makeyourmorning.utilities.alarm.AlarmScheduler
import com.nochunsam.makeyourmorning.utilities.database.AppRepository
import com.nochunsam.makeyourmorning.utilities.notification.NotificationPermission


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("LaunchDuringComposition")
@Composable
fun TimerScreen() {
    // 필요 변수 선언
    val context = LocalContext.current
    var selectedMinutes by remember { mutableIntStateOf(10) }
    val repository = remember { AppRepository(context.applicationContext as Application) }

    val dayCount by repository.getDayCount().collectAsState(initial = null)
    val isBlocking by FocusBlockingManager.isBlocking.collectAsState()

    // 알림 권한 확인
    var hasNotificationPermission by remember {
        mutableStateOf(NotificationPermission.hasNotificationPermission(context))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
        }
    )
    if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        LaunchedEffect(Unit) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // 화면
        Column (
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("총 ${dayCount?.count ?: 0}번 하루를 만들었어요!", fontSize = 18.sp)

            Text("이 시간동안 하루를 열 준비를 해봅시다!", fontSize = 20.sp)

            if (isBlocking) {
                CountdownCircularTimer()
            } else {
                CircularTimerPicker(
                    initialMinutes = selectedMinutes,
                    onTimeChange = { selectedMinutes = it }
                )
            }

            if(isBlocking) {
                Button(
                    onClick = {
                        FocusBlockingManager.currentBlockId?.let { AlarmScheduler(context).cancel(it) }
                        FocusBlockingManager.stopBlocking()
                    },
                ) {
                    Text("취소")
                }
            } else {
                Button(
                    onClick = {
                        val blockTime = BlockTime(minute = selectedMinutes,)
                        AlarmScheduler(context).scheduleBlock(blockTime)
                        FocusBlockingManager.startBlockingFor(
                            blockId = blockTime.id,
                            minutes = selectedMinutes
                        )
                    },
                ) {
                    Text("시작")
                }
            }
        }
    }

