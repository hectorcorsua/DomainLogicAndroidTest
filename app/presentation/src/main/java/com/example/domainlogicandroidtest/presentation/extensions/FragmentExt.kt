package com.example.domainlogicandroidtest.presentation.extensions

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

fun Fragment.alert(dialogBuilder: AlertDialog.Builder.() -> Unit): AlertDialog {
    val builder = AlertDialog.Builder(requireContext())
    builder.dialogBuilder()
    return builder.create()
}