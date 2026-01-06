package com.nochunsam.makeyourmorning.utilities.alarm

import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.nochunsam.makeyourmorning.R
import com.nochunsam.makeyourmorning.pages.main.MainActivity
import com.nochunsam.makeyourmorning.utilities.database.AppRepository
import com.nochunsam.makeyourmorning.utilities.notification.NotificationPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null){
            return
        }

        Log.d("Debug", "알림을 받았습니다.")

        val repo = AppRepository(context.applicationContext as Application)
        val minute = intent.getIntExtra("com.nochunsam.makeyourmorning.Minute", 0)
        CoroutineScope(Dispatchers.IO).launch {
            repo.insertDayRecord(minute)
        }
        fireNotification(context)
    }

    private fun fireNotification(context: Context) {
        val hasPermission = NotificationPermission.hasNotificationPermission(context)

        if (!hasPermission) {
            return
        }

        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingMain = PendingIntent.getActivity(
            context,
            2001,
            mainIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, "make_your_morning5134")
            .setSmallIcon(R.drawable.ic_wake)
            .setContentTitle("시간이 끝났어요!")
            .setContentText("좋은 하루되세요! 😊")
            .setContentIntent(pendingMain)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1001, notification)
    }
}