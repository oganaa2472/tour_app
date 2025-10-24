package com.example.survey.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.survey.data.local.converter.DateConverter
import com.example.survey.data.local.dao.TourDao
import com.tourexplorer.app.data.local.dao.BookingDao

import com.example.survey.data.local.entity.BookingEntity
import com.example.survey.data.local.entity.TourEntity
@Database(
    entities = [TourEntity::class, BookingEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class TourDatabase : RoomDatabase() {
    abstract fun tourDao(): TourDao
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: TourDatabase? = null

        fun getDatabase(context: Context): TourDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TourDatabase::class.java,
                    "tour_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}