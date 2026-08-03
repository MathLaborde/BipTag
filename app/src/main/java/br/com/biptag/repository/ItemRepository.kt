package br.com.biptag.repository

import android.util.Log
import androidx.compose.runtime.remember
import br.com.biptag.model.Item
import br.com.biptag.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class ItemRepository {
    private val postgrest = SupabaseClient.client.from("items")
    private val storage = SupabaseClient.client.storage
    suspend fun getAllItems(): List<Item> {
        val authRepository = AuthRepository()

        val user = authRepository.getCurrentUser() ?: return emptyList()

        return postgrest.select(columns = Columns.raw("*, category_data:category(*)")) {
            filter {
                eq("user_id", user.id)
            }
        }.decodeList<Item>()
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

    suspend fun updateItem(item: Item) {
        try {
            val dadosParaAtualizar = Item(
                name = item.name,
                description = item.description,
                category = item.category,
                image = item.image,
                status = item.status,
                tagId = item.tagId
            )

            postgrest.update(dadosParaAtualizar) {
                filter {
                    eq("id", item.id ?: 0)
                }
            }
        } catch (e: Exception) {
            Log.e("ItemRepository", "Erro ao atualizar item: ${item.id}", e)
            throw e
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

    suspend fun deleteItem(id: Int) {
        try {
            postgrest.delete {
                filter {
                    eq("id", id)
                }
            }
        } catch (e: Exception) {
            Log.e("ItemRepository", "Erro ao deletar item: $id", e)
            throw e
        }
    }

    suspend fun saveItem(item: Item): Item {
        try {
            return postgrest.insert(item) {
                select()
            }.decodeSingle<Item>()
        } catch (e: Exception) {
            Log.e("ItemRepository", "Erro ao salvar novo item", e)
            throw e
        }
    }

    suspend fun updateStatus(id: Int, status: String): Boolean {
        return try {
            postgrest.update({
                set("status", status)
            }) {
                filter {
                    eq("id", id)
                }
            }
            true
        } catch (e: Exception) {
            Log.e("ItemRepository", "Erro ao deletar item: $id", e)
            false
        }
    }
}
