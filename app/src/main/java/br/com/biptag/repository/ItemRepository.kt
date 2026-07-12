package br.com.biptag.repository

import android.util.Log
import br.com.biptag.model.Item
import br.com.biptag.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage

class ItemRepository {
    private val postgrest = SupabaseClient.client.from("items")
    private val storage = SupabaseClient.client.storage
    suspend fun getAllItems(): List<Item> {
        return postgrest.select(columns = Columns.raw("*, category_data:category(*)"))
            .decodeList<Item>()
    }

    suspend fun getItemById(id: Int): Item? {
        return try {
            postgrest.select(columns = Columns.raw("*, category_data:category(*)")) {
                filter {
                    eq("id", id)
                }
            }.decodeSingleOrNull<Item>()
        } catch (e: Exception) {
            Log.e("ItemRepository", "Erro ao buscar item pelo ID: $id", e)
            null
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

    suspend fun saveItem(item: Item) {
        postgrest.insert(item)
    }
}
