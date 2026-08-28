package br.com.biptag.repository

import android.util.Log
import br.com.biptag.model.Category
import br.com.biptag.network.RetrofitClient
import br.com.biptag.network.SupabaseClient
import io.github.jan.supabase.postgrest.from

class CategoryRepository {
    private val apiService = RetrofitClient.categoryApiService

    suspend fun getAllItems(): List<Category> {
        return try {
            apiService.getAllCategories()
        } catch (e: Exception) {
            Log.e("CategoryRepository", "Erro ao buscar categorias via API", e)
            emptyList()
        }
    }
}