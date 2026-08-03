package br.com.biptag.repository

import br.com.biptag.model.ReturnProcess
import br.com.biptag.model.Review
import br.com.biptag.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class RatingRepository {
    private val postgrest = SupabaseClient.client.from("reviews")

    suspend fun create(review: Review): Review? {
        return try {
            postgrest.insert(review) {
                select()
            }.decodeSingleOrNull<Review>()
        } catch (e: Exception){
            null
        }
    }
}