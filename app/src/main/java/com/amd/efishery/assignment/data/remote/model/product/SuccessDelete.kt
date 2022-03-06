package com.amd.efishery.assignment.data.remote.model.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SuccessDelete(

	@SerializedName("clearedRowsCount")
	val clearedRowsCount: Int? = null
) : Parcelable
