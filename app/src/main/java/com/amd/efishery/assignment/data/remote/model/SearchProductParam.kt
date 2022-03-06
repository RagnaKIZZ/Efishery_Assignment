package com.amd.efishery.assignment.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchProductParam(

	@field:SerializedName("size")
	val size: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("komoditas")
	val komoditas: String? = null,

	@field:SerializedName("uuid")
	val uuid: String? = null,

	@field:SerializedName("tgl_parsed")
	val tglParsed: String? = null,

	@field:SerializedName("area_provinsi")
	val areaProvinsi: String? = null,

	@field:SerializedName("area_kota")
	val areaKota: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
) : Parcelable
