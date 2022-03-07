package com.amd.efishery.assignment.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amd.efishery.assignment.BuildConfig
import com.amd.efishery.assignment.data.local.dao.OptionAreaDao
import com.amd.efishery.assignment.data.local.dao.OptionSizeDao
import com.amd.efishery.assignment.data.local.dao.ProductDao
import com.amd.efishery.assignment.data.local.dao.RemoteKeyDao
import com.amd.efishery.assignment.data.local.entity.OptionAreaEntity
import com.amd.efishery.assignment.data.local.entity.OptionSizeEntity
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.local.entity.RemoteKey

@Database(
    entities = [ProductEntity::class, OptionAreaEntity::class, OptionSizeEntity::class, RemoteKey::class],
    version = 2,
    exportSchema = false
)
abstract class LocalDb : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun sizeDao(): OptionSizeDao
    abstract fun areaDao(): OptionAreaDao
    abstract fun remoteKeyDao(): RemoteKeyDao

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