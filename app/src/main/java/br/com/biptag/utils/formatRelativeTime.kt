package br.com.biptag.utils

import java.time.Duration
import java.time.Instant

fun formatRelativeTime(isoString: String?): String {
    if (isoString.isNullOrEmpty()) return ""
    return try {
        val instant = Instant.parse(isoString)
        val now = Instant.now()
        val duration = Duration.between(instant, now)
        val seconds = duration.seconds

        when {
            seconds < 60 -> "Agora mesmo"
            seconds < 3600 -> "Há ${seconds / 60} min"
            seconds < 86400 -> "Há ${seconds / 3600} h"
            seconds < 2592000 -> "Há ${seconds / 86400} dias"
            else -> "Há ${seconds / 2592000} meses"
        }
    } catch (e: Exception) {
        isoString ?: ""
    }
}


