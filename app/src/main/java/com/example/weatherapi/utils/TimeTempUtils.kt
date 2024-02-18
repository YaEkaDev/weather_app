package com.example.weatherapi.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun tempToInt(temp: String?):String{
    if (temp==null) return ""
    return temp.toFloat().toInt().toString()
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDate(date: String?, tag: String = "f1"):String{

    if (date==null) return ""
    val d: String
    val m: String

    if (tag=="f0"){
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val parsedDateTime = LocalDateTime.parse(date,formatter)
        d = parsedDateTime.dayOfMonth.toString()
        m = parsedDateTime.month.toString()
    }
    else {
        val parsedDate = LocalDate.parse(date)
        d = parsedDate.dayOfMonth.toString()
        m = parsedDate.month.toString()
    }

   val shortM = when(m){
        "JANUARY" -> "JAN"
        "FEBRUARY" -> "FEB"
        "MARCH" -> "MAR"
        "APRIL" -> "APR"
        "JUNE" -> "JUN"
        "JULY" -> "JUL"
        "AUGUST" -> "AUG"
        "SEPTEMBER" -> "SEP"
        "OCTOBER" -> "OCT"
        "NOVEMBER" -> "NOV"
        "DECEMBER" -> "DEC"
        else -> ""
    }

    val str= String.format("%s %s",d,shortM)
    return str


}
@RequiresApi(Build.VERSION_CODES.O)
fun getHour(date: String?):String{

    if (date==null) return ""

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val parsedDate = LocalDateTime.parse(date,formatter)
    val hour = parsedDate.hour

    val format = when(hour){
        in 0..11 -> "AM"
        else -> "PM"
    }

    val h = when(hour){
        0, 12 -> 12
        in 1..11 -> hour
        else -> {hour-12}
    }

    return "$h $format"

}