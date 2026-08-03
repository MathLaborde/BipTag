package br.com.biptag.utils

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Formata uma string de data no formato ISO (ex: 2026-06-03T10:00:00+00:00)
 * para o formato brasileiro (dd/MM/yyyy).
 */
fun formatToBRDate(isoDate: String?): String {
    if (isoDate.isNullOrBlank()) return ""
    return try {
        val date = OffsetDateTime.parse(isoDate)

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("pt", "BR"))
        date.format(formatter)
    } catch (e: Exception) {
        isoDate
    }
}

/**
 * Formata uma string de data no formato ISO (ex: 2026-06-03T10:00:00+00:00)
 * para o formato brasileiro com hora (dd/MM/yyyy HH:mm).
 */
fun formatToBRDateTime(isoDate: String?): String {
    if (isoDate.isNullOrBlank()) return ""
    return try {
        val date = OffsetDateTime.parse(isoDate)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
        date.format(formatter)
    } catch (e: Exception) {
        isoDate
    }
}
