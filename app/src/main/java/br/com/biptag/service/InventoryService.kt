package br.com.biptag.service

import retrofit2.Call
import br.com.biptag.model.Inventory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface InventoryService {

    @GET("rest/v1/itens")
    fun getAllInventories(
        @Header("apikey") apikey: String,
        @Header("Authorization") authToken: String
    ): Call <List<Inventory>>

    @POST("rest/v1/itens")
    fun insertInventory(
        @Header("apikey") apikey: String,
        @Header("Authorization") authToken: String,
        @Body inventory: Inventory,
        @Header("Prefer") prefer: String = "return=representation"
    ): Call <List<Inventory>>
}