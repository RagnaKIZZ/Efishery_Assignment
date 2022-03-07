package com.amd.efishery.assignment.data.local.dao

import androidx.room.*
import com.amd.efishery.assignment.data.local.entity.OptionAreaEntity

@Dao
interface OptionAreaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArea(list: List<OptionAreaEntity>)

    @Query("SELECT * FROM optionAreaEntity")
    suspend fun getAllArea(): List<OptionAreaEntity>

    @Query("SELECT * FROM optionAreaEntity WHERE province = :province")
    suspend fun getAllAreaByProvince(province: String): List<OptionAreaEntity>

    @Query("DELETE FROM optionAreaEntity")
    suspend fun deleteArea()

}