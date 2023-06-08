package com.example.pakaianapanich.utils


import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun CurrentDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    val formattedDate = currentDate.format(formatter)
    return formattedDate
}