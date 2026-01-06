package com.nochunsam.makeyourmorning.utilities.splash

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nochunsam.makeyourmorning.utilities.pref.PrefDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val prefDataStore = PrefDataStore(application)
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    // 시작 화면 결정
    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            // 1. DataStore 체크 (비동기)
            val isFirst = prefDataStore.isFirstOpen.first() // 첫 값만 읽고 종료

            Log.d("SplashViewModel", "isFirst: $isFirst")

            _startDestination.value = if (isFirst) "intro" else "main"

            Log.d("SplashViewModel", "startDestination: ${startDestination.value}")

            // 2. 스플래시 해제
            _isLoading.value = false
        }
    }

    fun setIntroFinished() {
        viewModelScope.launch {
            prefDataStore.setFirstOpen()
        }
    }
}