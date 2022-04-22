package com.example.ink4youapp.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import io.github.tonnyl.light.Light

object SnackBar {
    fun showSnackBar(view: View, status: String, message: String) {
        if (status == "success") {
            Light.success(view, message, Snackbar.LENGTH_SHORT).show()
        } else if (status == "error") {
            Light.error(view, message, Snackbar.LENGTH_SHORT).show()
        }
    }
}
