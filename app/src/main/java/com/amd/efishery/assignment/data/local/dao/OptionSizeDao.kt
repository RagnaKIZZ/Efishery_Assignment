package com.amd.efishery.assignment.data.local.dao

import androidx.room.*
import com.amd.efishery.assignment.data.local.entity.OptionSizeEntity

@Dao
interface OptionSizeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSize(list: List<OptionSizeEntity>)

    @Query("SELECT * FROM optionSizeEntity")
    suspend fun getAllSize(): List<OptionSizeEntity>

    @Query("DELETE FROM optionSizeEntity")
    suspend fun deleteSize()

}