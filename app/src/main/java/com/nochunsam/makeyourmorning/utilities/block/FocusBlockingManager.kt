package com.nochunsam.makeyourmorning.utilities.block

import android.os.SystemClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object FocusBlockingManager {
    private val scope = CoroutineScope(Dispatchers.Default)
    private var timerJob: Job? = null

    private val _isBlocking = MutableStateFlow(false)
    val isBlocking: StateFlow<Boolean> = _isBlocking.asStateFlow()

    private val _remainingSeconds = MutableStateFlow(0)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private var startTime: Long = 0L
    private var totalSeconds: Int = 0

    var currentBlockId: String? = null
        private set

    fun startBlockingFor(blockId: String, minutes: Int) {
        // 기존에 존재하는 job 취소
        timerJob?.cancel()

        startTime = SystemClock.elapsedRealtime()
        totalSeconds = minutes * 60
        currentBlockId = blockId
        _isBlocking.value = true

        timerJob = scope.launch {
            while (true) {
                val elapsed = ((SystemClock.elapsedRealtime() - startTime) / 1000).toInt()
                val remaining = (totalSeconds - elapsed).coerceAtLeast(0)

                _remainingSeconds.value = remaining
                _progress.value = if (totalSeconds > 0) remaining / totalSeconds.toFloat() else 0f

                if (remaining == 0) {
                    stopBlocking()
                    break
                }
                delay(1000)
            }
        }
    }

    fun stopBlocking() {
        timerJob?.cancel()
        timerJob = null
        startTime = 0L
        totalSeconds = 0
        currentBlockId = null
        _isBlocking.value = false
        _remainingSeconds.value = 0
        _progress.value = 0f
    }
}