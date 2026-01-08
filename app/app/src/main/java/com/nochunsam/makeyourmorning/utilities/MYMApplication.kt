package com.nochunsam.makeyourmorning.utilities

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.nochunsam.makeyourmorning.utilities.database.AppDatabase
import com.nochunsam.makeyourmorning.utilities.database.AppRepository

class MYMApplication: Application() {
    private val database by lazy { AppDatabase.getInstance(this)!! }
    val repository by lazy { AppRepository(database.dayRecordDao()) }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =  NotificationChannel(
                "make_your_morning5134",
                "sleep time alarm",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


    }
    override fun onTerminate() {
        super.onTerminate()
    }
}