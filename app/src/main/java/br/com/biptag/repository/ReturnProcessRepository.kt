package br.com.biptag.repository

import android.util.Log
import br.com.biptag.model.ReturnProcess
import br.com.biptag.network.RetrofitClient
import br.com.biptag.network.SupabaseClient
import io.github.jan.supabase.auth.auth

class ReturnProcessRepository {
    private val api = RetrofitClient.returnProcessService
    private val auth = SupabaseClient.client.auth

    private fun getBearerToken(): String {
        val token = auth.currentAccessTokenOrNull() ?: ""
        return "Bearer $token"
    }

    suspend fun createReturnProcess(process: ReturnProcess): ReturnProcess? {
        return try {
            val response = api.createReturnProcess(getBearerToken(), process)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("ReturnProcessRepository", "Erro na API: ${response.code()} - ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ReturnProcessRepository", "Erro ao criar processo de devolução", e)
            null
        }
    }

    suspend fun getReturnProcessById(id: Int): ReturnProcess? {
        return try {
            val response = api.getAllReturnProcesses(getBearerToken())
            if (response.isSuccessful) {
                response.body()?.find { it.id == id }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ReturnProcessRepository", "Erro ao buscar processo de devolução", e)
            null
        }
    }

    suspend fun updateStatus(id: Int, status: String): Boolean {
        return try {
            val currentList = api.getAllReturnProcesses(getBearerToken()).body()
            val existing = currentList?.find { it.id == id } ?: return false

            val updatedObj = existing.copy(status = status)
            val response = api.updateReturnProcess(getBearerToken(), id, updatedObj)

            response.isSuccessful
        } catch (e: Exception) {
            Log.e("ReturnProcessRepository", "Erro ao atualizar status", e)
            false
        }
    }
}