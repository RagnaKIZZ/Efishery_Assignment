package com.amd.efishery.assignment.utils

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.amd.efishery.assignment.R
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

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun EditText.listenError() {
    this.addTextChangedListener {
        this.error = null
    }
}

fun View.validateField() {
    when (this) {
        is AutoCompleteTextView -> {
            if (this.text.isNullOrEmpty()) {
                this.error = this.context.getString(R.string.required)
                return
            }
        }

        is EditText -> {
            if (this.text.isNullOrEmpty()) {
                this.error = this.context.getString(R.string.required)
                return
            }
        }
    }
}

fun Context.showAlertDialog(
    title: String,
    msg: String,
    onClick: (() -> Unit)? = null
) {
    AlertDialog.Builder(this)
        .setIcon(R.mipmap.ic_launcher)
        .setTitle(title)
        .setMessage(msg)
        .setPositiveButton("Ya") { dialog, _ ->
            onClick?.invoke().also {
                dialog.dismiss()
            }
        }
        .setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }.show()
}