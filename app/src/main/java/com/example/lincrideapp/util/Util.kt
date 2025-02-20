package com.example.lincrideapp.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getGreeting(): String {
    val calendar = Calendar.getInstance()
    val timeOfDay = calendar.get(Calendar.HOUR_OF_DAY)

    return when(timeOfDay) {
        in 0..11 -> "Good morning, "
        in 12..16 -> "Good afternoon, "
        else -> "Good evening, "
    }
}

fun getFormattedAmt(input: Double, currency: String): String {
    //Log.e(TAG, "INPUT TO BE FORMATTED: " + input);
    val pattern = "#,##0.00"
    val decimalFormat = DecimalFormat(pattern)
    //"₦ "
    return currency + decimalFormat.format(input.toLong())
}
fun getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Date())
}