package br.com.biptag.repository

import android.util.Log
import br.com.biptag.model.Alert
import br.com.biptag.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class AlertRepository {
    private val postgrest = SupabaseClient.client.from("alerts")

    suspend fun getActiveAlerts(): List<Alert> {
        return try {
            postgrest.select(columns = Columns.raw("*, item_data:items(*)")) {
                filter {
                    eq("status", "active")
                }
            }.decodeList<Alert>()
        } catch (e: Exception) {
            Log.e("AlertRepository", "Erro ao buscar alertas ativos", e)
            emptyList()
        }
    }

    suspend fun getAlertById(id: Int): Alert? {
        return try {
            postgrest.select(columns = Columns.raw("*, item_data:items(*)")) {
                filter {
                    eq("id", id)
                }
            }.decodeSingleOrNull<Alert>()
        } catch (e: Exception) {
            Log.e("AlertRepository", "Erro ao buscar alerta pelo ID: $id", e)
            null
        }
    }

    suspend fun createAlert(alert: Alert) {
        try {
            postgrest.insert(alert)
        } catch (e: Exception) {
            Log.e("AlertRepository", "Erro ao criar alerta", e)
            throw e
        }
    }
}