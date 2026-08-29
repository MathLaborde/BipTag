package br.com.biptag.repository

import android.util.Log
import br.com.biptag.model.PartnerPoint
import br.com.biptag.network.RetrofitClient
import br.com.biptag.network.SupabaseClient
import io.github.jan.supabase.auth.auth

class PartnerPointRepository {
    private val api = RetrofitClient.partnerPointService
    private val auth = SupabaseClient.client.auth

    private fun getBearerToken(): String {
        val token = auth.currentAccessTokenOrNull() ?: ""
        return "Bearer $token"
    }

    suspend fun getAllPartnerPoints(): List<PartnerPoint> {
        return try {
            val response = api.getAllPartnerPoints(getBearerToken())
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("PartnerPointRepository", "Erro na API: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("PartnerPointRepository", "Erro ao buscar pontos parceiros", e)
            emptyList()
        }
    }

    suspend fun getPartnerPointById(id: Int?): PartnerPoint? {
        if (id == null) return null
        return try {
            val response = api.getPartnerPointById(getBearerToken(), id)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("PartnerPointRepository", "API retornou erro ao buscar ID $id: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("PartnerPointRepository", "Erro ao buscar ponto parceiro por ID", e)
            null
        }
    }
}