package com.amd.efishery.assignment.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amd.efishery.assignment.data.local.entity.RemoteKey

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM remoteKey WHERE productId = :id")
    suspend fun remoteKeysProductId(id: String): RemoteKey?

    @Query("DELETE FROM remoteKey")
    suspend fun deleteAll()
}

