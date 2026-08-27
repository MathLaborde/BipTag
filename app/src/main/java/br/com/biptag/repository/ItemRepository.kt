package br.com.biptag.repository

import android.util.Log
import br.com.biptag.model.Item
import br.com.biptag.services.RetrofitClient
import br.com.biptag.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage

class ItemRepository {

    private val storage = SupabaseClient.client.storage

    private fun getBearerToken(): String {
        val token = SupabaseClient.client.auth.currentAccessTokenOrNull() ?: ""
        return "Bearer $token"
    }

    suspend fun getAllItems(): List<Item> {
        return try {
            RetrofitClient.apiService.getItems(getBearerToken())
        } catch (e: Exception) {
            Log.e("ItemRepository", "Erro ao buscar itens da API", e)
            emptyList()
        }
    }

    suspend fun getItemById(id: Int): Item? {
        return try {
            RetrofitClient.apiService.getItemById(getBearerToken(), id)
        } catch (e: Exception) {
            Log.e("ItemRepository", "Erro ao buscar item pelo ID: $id", e)
            null
        }
    }

    suspend fun saveItem(item: Item): Item {
        return try {
            RetrofitClient.apiService.saveItem(getBearerToken(), item)
        } catch (e: Exception) {
            Log.e("ItemRepository", "Erro ao salvar novo item na API", e)
            throw e
        }
    }

    suspend fun updateItem(item: Item) {
        try {
            val id = item.id ?: throw IllegalArgumentException("Item ID não pode ser nulo")
            RetrofitClient.apiService.updateItem(getBearerToken(), id, item)
        } catch (e: Exception) {
            Log.e("ItemRepository", "Erro ao atualizar item: ${item.id}", e)
            throw e
        }
    }

    suspend fun deleteItem(id: Int) {
        try {
            RetrofitClient.apiService.deleteItem(getBearerToken(), id)
        } catch (e: Exception) {
            Log.e("ItemRepository", "Erro ao deletar item: $id", e)
            throw e
        }
    }

    suspend fun updateStatus(id: Int, status: String): Boolean {
        return try {
            val body = mapOf("status" to status)
            RetrofitClient.apiService.updateItemStatus(getBearerToken(), id, body)
            true
        } catch (e: Exception) {
            Log.e("ItemRepository", "Erro ao atualizar status do item: $id", e)
            false
        }
    }

    suspend fun uploadImage(userId: String, fileName: String, byteArray: ByteArray): String {
        val bucket = storage.from("ItemImage")
        val path = "$userId/$fileName"
        bucket.upload(path, byteArray) {
            upsert = true
        }
        return bucket.publicUrl(path)
    }
}