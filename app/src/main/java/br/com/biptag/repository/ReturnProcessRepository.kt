package br.com.biptag.repository

import android.util.Log
import br.com.biptag.model.ReturnProcess
import br.com.biptag.supabase.SupabaseClient
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
}