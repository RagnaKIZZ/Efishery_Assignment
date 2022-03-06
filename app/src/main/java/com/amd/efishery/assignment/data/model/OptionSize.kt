package com.amd.efishery.assignment.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OptionSize(

	@field:SerializedName("size")
	val size: String? = null
) : Parcelable
