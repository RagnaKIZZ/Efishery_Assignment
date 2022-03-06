package com.amd.efishery.assignment.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity
@Parcelize
data class ProductEntity(

    var size: String? = null,

    var price: String? = null,

    var komoditas: String? = null,

    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString(),

    var tglParsed: String? = null,

    var areaProvinsi: String? = null,

    var areaKota: String? = null,

    var timestamp: String? = null

) : Parcelable
