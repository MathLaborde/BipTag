package br.com.biptag.repository

import br.com.biptag.model.Category
import br.com.biptag.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class CategoryRepository {
    private val postgrest = SupabaseClient.client.from("category")

    suspend fun getAllItems(): List<Category> {
        return postgrest.select()
            .decodeList<Category>()
    }
}