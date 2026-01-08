package com.nochunsam.makeyourmorning.utilities.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nochunsam.makeyourmorning.common.data.DayRecord
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DatabaseViewModel(private val repository: AppRepository) : ViewModel() {

    val earliestRecords: StateFlow<List<DayRecord>> = repository.getEarliestRecordPerDay()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val dayCount: StateFlow<Int> = repository.getDayCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    fun clearAllData() {
        viewModelScope.launch {
            try {
                repository.truncateTable()
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    class Factory(private val repository: AppRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DatabaseViewModel::class.java)) {
                return DatabaseViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}