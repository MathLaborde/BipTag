package br.com.biptag.services

import br.com.biptag.model.Item
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BiptagApiService {

    @GET("/api/v1/items")
    suspend fun getItems(@Header("Authorization") token: String): List<Item>

    @GET("/api/v1/items/{id}")
    suspend fun getItemById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Item

    @POST("/api/v1/items")
    suspend fun saveItem(
        @Header("Authorization") token: String,
        @Body item: Item
    ): Item

    @PUT("/api/v1/items/{id}")
    suspend fun updateItem(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body item: Item
    ): Item

    @DELETE("/api/v1/items/{id}")
    suspend fun deleteItem(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    )

    @PATCH("/api/v1/items/{id}/status")
    suspend fun updateItemStatus(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body statusMap: Map<String, String>
    )
}