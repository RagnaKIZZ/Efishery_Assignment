package com.amd.efishery.assignment.data.remote.model.area

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OptionArea(

    @field:SerializedName("province")
    val province: String? = null,

    @field:SerializedName("city")
    val city: String? = null
) : Parcelable
