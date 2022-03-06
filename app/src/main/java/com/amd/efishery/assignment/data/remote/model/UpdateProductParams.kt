package com.amd.efishery.assignment.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateProductParams(

	@field:SerializedName("condition")
	var condition: Condition? = null,

	@field:SerializedName("set")
	var set: Set? = null,

	@field:SerializedName("limit")
	var limit: Int? = null
) : Parcelable

@Parcelize
data class Set(

	@field:SerializedName("size")
	var size: String? = null,

	@field:SerializedName("price")
	var price: String? = null,

	@field:SerializedName("komoditas")
	var komoditas: String? = null,

	@field:SerializedName("area_provinsi")
	var areaProvinsi: String? = null,

	@field:SerializedName("area_kota")
	var areaKota: String? = null
) : Parcelable
