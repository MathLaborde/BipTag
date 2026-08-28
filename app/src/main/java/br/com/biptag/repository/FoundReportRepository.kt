package br.com.biptag.repository

import android.util.Log
import br.com.biptag.model.FoundReport
import br.com.biptag.network.RetrofitClient
import br.com.biptag.network.SupabaseClient
import io.github.jan.supabase.auth.auth

class FoundReportRepository {
    private val api = RetrofitClient.foundReportService
    private val auth = SupabaseClient.client.auth

    private fun getBearerToken(): String {
        val token = auth.currentAccessTokenOrNull() ?: ""
        return "Bearer $token"
    }

    suspend fun createFoundReport(report: FoundReport): FoundReport? {
        return try {
            val response = api.createFoundReport(getBearerToken(), report)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("FoundReportRepository", "Erro na API: ${response.code()} - ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("FoundReportRepository", "Erro ao criar registro de encontro", e)
            null
        }
    }

    suspend fun getFoundReportById(id: Int): FoundReport? {
        return try {
            val response = api.getFoundReportById(getBearerToken(), id)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("FoundReportRepository", "API retornou erro ao buscar ID $id: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("FoundReportRepository", "Erro ao buscar registro pelo ID: $id", e)
            null
        }
    }

    suspend fun getFoundReportByAlertId(alertId: Int): FoundReport? {
        return try {
            val response = api.getFoundReportByAlertId(getBearerToken(), alertId)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("FoundReportRepository", "API retornou erro ao buscar Alert ID $alertId: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("FoundReportRepository", "Erro ao buscar registro pelo Alert ID: $alertId", e)
            null
        }
    }
}