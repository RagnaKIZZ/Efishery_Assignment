package com.amd.efishery.assignment.data.remote.model.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchProductParam(

	@field:SerializedName("size")
	var size: String? = null,

	@field:SerializedName("komoditas")
	var komoditas: String? = null,

	@field:SerializedName("area_provinsi")
	var areaProvinsi: String? = null,

	@field:SerializedName("area_kota")
	var areaKota: String? = null,

	@field:SerializedName("timestamp")
	var timestamp: String? = null
) : Parcelable
