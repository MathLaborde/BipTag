package br.com.biptag.repository

import br.com.biptag.model.Item
import br.com.biptag.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class ItemRepository {
    private val postgrest = SupabaseClient.client.from("items")

    suspend fun getAllItems(): List<Item> {
        return postgrest.select(columns = Columns.raw("*, category_data:category(*)"))
            .decodeList<Item>()
    }
}
