package com.amd.efishery.assignment.domain.mapper

import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.remote.model.product.ProductItem
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