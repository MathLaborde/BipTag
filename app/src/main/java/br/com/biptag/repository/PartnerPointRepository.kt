package br.com.biptag.repository

import android.util.Log
import br.com.biptag.model.PartnerPoint
import br.com.biptag.network.SupabaseClient
import io.github.jan.supabase.postgrest.from

class PartnerPointRepository {
    private val postgrest = SupabaseClient.client.from("partner_points")

    suspend fun getAllPartnerPoints(): List<PartnerPoint> {
        return try {
            postgrest.select().decodeList<PartnerPoint>()
        } catch (e: Exception) {
            Log.e("PartnerPointRepository", "Erro ao buscar pontos parceiros", e)
            emptyList()
        }
    }

    suspend fun getPartnerPointById(id: Int?): PartnerPoint? {
        if (id == null) return null
        return try {
            postgrest.select {
                filter { eq("id", id) }
            }.decodeSingleOrNull<PartnerPoint>()
        } catch (e: Exception) {
            Log.e("PartnerPointRepository", "Erro ao buscar ponto parceiro por ID", e)
            null
        }
    }
}