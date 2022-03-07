package com.amd.efishery.assignment.utils

import android.util.Log
import java.text.NumberFormat
import java.util.*

fun Double.toRp(): String {
    val localeID = Locale("in", "ID")
    val formatRupiah = NumberFormat.getInstance(localeID)
    return "Rp.${formatRupiah.format(this)}"
}

fun Int.toRp(): String {
    val localeID = Locale("in", "ID")
    val formatRupiah = NumberFormat.getInstance(localeID)
    return "Rp.${formatRupiah.format(this.toDouble())}"
}

fun String.toRp(): String {
    val localeID = Locale("in", "ID")
    val formatRupiah = NumberFormat.getInstance(localeID)
    return "Rp.${formatRupiah.format(this.toDouble())}"
}

fun Any.logging(msg: String, tag: String = javaClass.name.toString()) {
    Log.d(tag.uppercase(), msg)
}