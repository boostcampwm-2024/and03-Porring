package com.kolown.network

import android.icu.util.Calendar
import android.os.Build
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class PorringDateTime {
    companion object {
        fun getNowDateTimeString(): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                current.format(formatter)
            } else {
                // API 26 이하 대응
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1 // Month is zero-based
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                val second = calendar.get(Calendar.SECOND)
                String.format(
                    Locale.US,
                    "%04d%02d%02d%02d%02d%02d",
                    year, month, day, hour, minute, second
                )
            }
        }

        fun getNowDateTimeUTCString(): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.now().toString()
            } else {
                // API 26 이하 대응
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1 // Month is zero-based
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                val second = calendar.get(Calendar.SECOND)
                String.format(
                    Locale.US,
                    "%04d-%02d-%02dT%02d:%02d:%02d",
                    year, month, day, hour, minute, second
                )
            }
        }
    }
}
