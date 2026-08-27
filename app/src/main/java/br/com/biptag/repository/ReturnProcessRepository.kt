package br.com.biptag.repository

import android.util.Log
import br.com.biptag.model.ReturnProcess
import br.com.biptag.network.SupabaseClient
import io.github.jan.supabase.postgrest.from

class ReturnProcessRepository {
    private val postgrest = SupabaseClient.client.from("return_processes")

    suspend fun createReturnProcess(process: ReturnProcess): ReturnProcess? {
        return try {
            postgrest.insert(process) {
                select()
            }.decodeSingleOrNull<ReturnProcess>()
        } catch (e: Exception) {
            Log.e("ReturnProcessRepository", "Erro ao criar processo de devolução", e)
            null
        }
    }

    suspend fun getReturnProcessById(id: Int): ReturnProcess? {
        return try {
            postgrest.select {
                filter { eq("id", id) }
            }.decodeSingleOrNull<ReturnProcess>()
        } catch (e: Exception) {
            Log.e("ReturnProcessRepository", "Erro ao buscar processo de devolução", e)
            null
        }
    }

    suspend fun updateStatus(id: Int, status: String): Boolean {
        return try {
            postgrest.update ({
                set("status", status)
            }) {
                filter { eq("id", id)  }
            }

            true
        } catch (e: Exception) {
            Log.e("ReturnProcessRepository", "Erro ao atualizar processo de devolução", e)
            false
        }
    }
}