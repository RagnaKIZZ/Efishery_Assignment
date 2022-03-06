package com.amd.efishery.assignment.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class OptionSizeEntity(
	@PrimaryKey
	var size: Int
) : Parcelable
