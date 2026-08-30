import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun formatDateAgo(dateString: String): String {
    return try {
        val instant = Instant.parse(dateString)
        val now = Instant.now()
        val diffDays = ChronoUnit.DAYS.between(instant, now)
        when {
            diffDays < 1 -> "Hoje"
            diffDays == 1L -> "Ontem"
            diffDays < 7 -> "Há $diffDays dias"
            diffDays < 30 -> "Há ${diffDays / 7} semanas"
            diffDays < 365 -> "Há ${diffDays / 30} meses"
            else -> {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    .withZone(ZoneId.systemDefault())
                formatter.format(instant)
            }
        }
    } catch (e: Exception) {
        "Data inválida"
    }
}

fun formatToBRDateTime(dateString: String): String {
    return try {
        val instant = Instant.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } catch (e: Exception) {
        dateString
    }
}
