package br.com.biptag.repository

import android.util.Log
import br.com.biptag.model.FoundReport
import br.com.biptag.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class FoundReportRepository {
    private val postgrest = SupabaseClient.client.from("found_reports")

    suspend fun createFoundReport(report: FoundReport): FoundReport? {
        return try {
            postgrest.insert(report) {
                select()
            }.decodeSingleOrNull<FoundReport>()
        } catch (e: Exception) {
            Log.e("FoundReportRepository", "Erro ao criar registro de encontro", e)
            null
        }
    }

    suspend fun getFoundReportById(id: Int): FoundReport? {
        return try {
            postgrest.select {
                filter {
                    eq("id", id)
                }
            }.decodeSingleOrNull<FoundReport>()
        } catch (e: Exception) {
            Log.e("FoundReportRepository", "Erro ao buscar registro pelo ID: $id", e)
            null
        }
    }
}