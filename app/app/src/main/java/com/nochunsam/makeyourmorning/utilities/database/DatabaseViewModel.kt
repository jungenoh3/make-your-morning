package com.nochunsam.makeyourmorning.utilities.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DatabaseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppRepository(application)

    fun clearAllData() {
        viewModelScope.launch {
            try {
                repository.truncateTable()
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }
}