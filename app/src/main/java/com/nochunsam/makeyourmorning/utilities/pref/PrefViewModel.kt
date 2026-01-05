package com.nochunsam.makeyourmorning.utilities.pref

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nochunsam.makeyourmorning.common.data.DayCount
import com.nochunsam.makeyourmorning.utilities.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PrefViewModel(application: Application) : AndroidViewModel(application) {

    private val prefDataStore = PrefDataStore(application)
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    // 시작 화면 결정
    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            // 1. DB 초기화 로직 (IO 스레드 자동 처리)
            launch(Dispatchers.IO) {
                val dao = AppDatabase.getInstance(application)?.dayCountDao()
                if (dao?.get() == null) {
                    dao?.insert(DayCount(id = 1, count = 0))
                }
            }

            // 2. DataStore 체크 (비동기)
            val isFirst = prefDataStore.isFirstOpen.first() // 첫 값만 읽고 종료

            println("isFirst: ${isFirst}")

            _startDestination.value = if (isFirst) "intro" else "main"

            println("startDestination: ${startDestination.value}")

            // 3. 스플래시 해제
            _isLoading.value = false
        }
    }

    fun setIntroFinished() {
        viewModelScope.launch {
            prefDataStore.setFirstOpen()
        }
    }
}