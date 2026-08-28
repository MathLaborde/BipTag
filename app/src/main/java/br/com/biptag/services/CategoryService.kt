package br.com.biptag.services

import br.com.biptag.model.Category
import retrofit2.http.GET

interface CategoryService {
    @GET("api/v1/categories")
    suspend fun getAllCategories(): List<Category>
}