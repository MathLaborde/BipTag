package br.com.biptag.services

import br.com.biptag.model.Review
import retrofit2.http.*

interface ReviewService {

    @GET("/api/v1/reviews")
    suspend fun getAllReviews(
        @Header("Authorization") token: String
    ): List<Review>

    @GET("/api/v1/reviews/return-process/{returnProcessId}")
    suspend fun getReviewsByReturnProcess(
        @Header("Authorization") token: String,
        @Path("returnProcessId") returnProcessId: Int
    ): List<Review>

    @POST("/api/v1/reviews")
    suspend fun createReview(
        @Header("Authorization") token: String,
        @Body review: Review
    ): Review
}