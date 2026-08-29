package br.com.biptag.repository

import android.util.Log
import br.com.biptag.model.Review
import br.com.biptag.network.RetrofitClient
import br.com.biptag.network.SupabaseClient
import io.github.jan.supabase.auth.auth

class ReviewRepository {

    private fun getBearerToken(): String {
        val token = SupabaseClient.client.auth.currentAccessTokenOrNull() ?: ""
        return "Bearer $token"
    }

    suspend fun getAllReviews(): List<Review> {
        return try {
            RetrofitClient.reviewApiService.getAllReviews(getBearerToken())
        } catch (e: Exception) {
            Log.e("ReviewRepository", "Erro ao buscar todas as avaliações", e)
            emptyList()
        }
    }

    suspend fun getReviewsByReturnProcess(returnProcessId: Int): List<Review> {
        return try {
            RetrofitClient.reviewApiService.getReviewsByReturnProcess(getBearerToken(), returnProcessId)
        } catch (e: Exception) {
            Log.e("ReviewRepository", "Erro ao buscar avaliações do processo $returnProcessId", e)
            emptyList()
        }
    }

    suspend fun createReview(review: Review): Review? {
        return try {
            RetrofitClient.reviewApiService.createReview(getBearerToken(), review)
        } catch (e: Exception) {
            Log.e("ReviewRepository", "Erro ao criar avaliação", e)
            null
        }
    }
}