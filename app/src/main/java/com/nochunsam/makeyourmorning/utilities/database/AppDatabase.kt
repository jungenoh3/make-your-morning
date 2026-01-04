package com.nochunsam.makeyourmorning.utilities.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nochunsam.makeyourmorning.common.data.DayCount
import com.nochunsam.makeyourmorning.utilities.database.dao.DayCountDao

@TypeConverters(AppTypeConverter::class)
@Database(entities = [DayCount::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dayCountDao(): DayCountDao

    companion object {
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "appDB")
                        .fallbackToDestructiveMigration(false)
                        .build()
                }
            }
            return instance
        }
    }
}