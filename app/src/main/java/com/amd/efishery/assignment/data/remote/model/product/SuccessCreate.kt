package com.amd.efishery.assignment.data.remote.model.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SuccessCreate(

	@SerializedName("updatedRange")
	val updatedRange: String? = null
) : Parcelable
