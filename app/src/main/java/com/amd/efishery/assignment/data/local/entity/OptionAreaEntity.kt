package com.amd.efishery.assignment.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class OptionAreaEntity(

    val province: String? = null,

    @PrimaryKey
    val city: String

) : Parcelable
