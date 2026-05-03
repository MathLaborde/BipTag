package br.com.biptag.factory

import br.com.biptag.service.InventoryService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    const val BASE_URL = "https://pnfuzxrbsgvtvvcscdsn.supabase.co/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getInventoryService(): InventoryService {
        return retrofit.create(InventoryService::class.java)
    }
}