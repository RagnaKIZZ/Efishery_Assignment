package com.amd.efishery.assignment.domain.mapper

import com.amd.efishery.assignment.data.local.entity.OptionAreaEntity
import com.amd.efishery.assignment.data.local.entity.OptionSizeEntity
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.remote.model.area.OptionArea
import com.amd.efishery.assignment.data.remote.model.product.ProductItem
import com.amd.efishery.assignment.data.remote.model.size.OptionSize
import java.util.*

fun ProductItem.toEntity() = ProductEntity(
    uuid = this.uuid ?: UUID.randomUUID().toString(),
    komoditas = this.komoditas,
    size = this.size,
    price = this.price,
    areaProvinsi = this.areaProvinsi,
    areaKota = this.areaKota,
    tglParsed = this.tglParsed,
    timestamp = this.timestamp
)

fun ProductEntity.toResponse() = ProductItem(
    uuid = this.uuid ?: UUID.randomUUID().toString(),
    komoditas = this.komoditas,
    size = this.size,
    price = this.price,
    areaProvinsi = this.areaProvinsi,
    areaKota = this.areaKota,
    tglParsed = this.tglParsed,
    timestamp = this.timestamp
)

fun OptionSize.toEntity() = OptionSizeEntity(
    size = this.size ?: 0
)

fun OptionArea.toEntity() = OptionAreaEntity(
    province = this.province,
    city = this.city ?: "Unknown"
)