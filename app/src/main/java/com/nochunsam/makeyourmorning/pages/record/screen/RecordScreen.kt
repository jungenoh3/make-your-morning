package com.nochunsam.makeyourmorning.pages.record.screen

import android.app.Application
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.nochunsam.makeyourmorning.common.compose.CustomColumn
import com.nochunsam.makeyourmorning.utilities.database.AppRepository

@Composable
fun RecordScreen() {
    val context = LocalContext.current
    val repository = remember { AppRepository(context.applicationContext as Application) }
    val records = repository.getEarliestRecordPerDay().collectAsState(initial = null)

    CustomColumn {
        Text(records.value.toString())
    }
}