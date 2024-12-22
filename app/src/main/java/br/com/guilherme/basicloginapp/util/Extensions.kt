package br.com.guilherme.basicloginapp.util

import android.util.Patterns

fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
    return this.isNotEmpty() && emailRegex.matches(this)
}