package com.amd.efishery.assignment.data.local.entity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amd.efishery.assignment.BuildConfig

@Database(
    entities = [ProductEntity::class, OptionAreaEntity::class, OptionSizeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDb : RoomDatabase() {

    companion object {
        @Volatile
        private var instance: LocalDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            LocalDb::class.java,
            BuildConfig.APPLICATION_ID
        )
            .fallbackToDestructiveMigration()
            .build()
    }

}