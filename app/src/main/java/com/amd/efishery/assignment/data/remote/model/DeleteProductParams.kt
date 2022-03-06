package com.amd.efishery.assignment.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeleteProductParams(

	@SerializedName("condition")
	var condition: Condition? = null,

	@SerializedName("limit")
	var limit: Int? = 1
) : Parcelable

@Parcelize
data class Condition(

	@SerializedName("uuid")
	var uuid: String? = null
) : Parcelable
