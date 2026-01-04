package com.nochunsam.makeyourmorning.utilities.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.nochunsam.makeyourmorning.common.data.BlockTime
import java.util.Calendar

class AlarmScheduler (private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun scheduleBlock(item: BlockTime){
        Log.d("Debug", "알림을 보냅니다.")

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("com.nochunsam.makeyourmorning.Id", item.id)
            putExtra("com.nochunsam.makeyourmorning.Minute", item.minute)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmTime: Calendar = Calendar.getInstance()
        alarmTime.add(Calendar.MINUTE, item.minute)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTime.timeInMillis, // Calendar.getInstance().timeInMillis + 1 * 15 * 1000L,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                alarmTime.timeInMillis, // Calendar.getInstance().timeInMillis + 1 * 15 * 1000L,
                pendingIntent
            )
        }
    }

    fun cancel(id: String){
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                id.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        println("알림을 취소했습니다.")
    }
}