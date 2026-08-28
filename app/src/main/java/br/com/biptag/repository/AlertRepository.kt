package br.com.biptag.repository

import android.util.Log
import br.com.biptag.model.Alert
import br.com.biptag.network.RetrofitClient
import br.com.biptag.network.SupabaseClient
import io.github.jan.supabase.auth.auth

class AlertRepository {

    private fun getBearerToken(): String {
        val token = SupabaseClient.client.auth.currentAccessTokenOrNull() ?: ""
        return "Bearer $token"
    }

    suspend fun getActiveAlerts(): List<Alert> {
        return try {
            val alerts = RetrofitClient.alertApiService.getAllAlerts(getBearerToken())
            alerts.filter { it.status.equals("active", ignoreCase = true) }
        } catch (e: Exception) {
            Log.e("AlertRepository", "Erro ao buscar alertas ativos via API", e)
            emptyList()
        }
    }

    suspend fun getAlertById(id: Int): Alert? {
        return try {
            RetrofitClient.alertApiService.getAlertById(getBearerToken(), id)
        } catch (e: Exception) {
            Log.e("AlertRepository", "Erro ao buscar alerta pelo ID: $id", e)
            null
        }
    }

    suspend fun createAlert(alert: Alert) {
        try {
            RetrofitClient.alertApiService.createAlert(getBearerToken(), alert)
        } catch (e: Exception) {
            Log.e("AlertRepository", "Erro ao criar alerta via API", e)
            throw e
        }
    }
}