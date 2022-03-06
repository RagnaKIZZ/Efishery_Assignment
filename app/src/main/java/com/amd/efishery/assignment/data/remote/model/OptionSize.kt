package com.amd.efishery.assignment.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OptionSize(

	@field:SerializedName("size")
	val size: String? = null
) : Parcelable
