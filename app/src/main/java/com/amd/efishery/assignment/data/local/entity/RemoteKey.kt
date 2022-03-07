package com.amd.efishery.assignment.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class RemoteKey(
    @PrimaryKey val productId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
