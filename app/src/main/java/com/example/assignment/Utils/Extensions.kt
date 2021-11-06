package com.example.assignment.Utils

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.snackBar(msg: String, length: Int) {
    Snackbar.make(requireView(), msg, length).show()
}