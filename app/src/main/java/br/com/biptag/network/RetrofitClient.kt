package br.com.biptag.network

import br.com.biptag.services.AlertService
import br.com.biptag.services.CategoryService
import br.com.biptag.services.FoundReportService
import br.com.biptag.services.ItemService
import br.com.biptag.services.PartnerPointService
import br.com.biptag.services.ReturnProcessService
import br.com.biptag.services.ReviewService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val itemApiService: ItemService by lazy {
        retrofit.create(ItemService::class.java)
    }

    val categoryApiService: CategoryService by lazy {
        retrofit.create(CategoryService::class.java)
    }

    val alertApiService: AlertService by lazy {
        retrofit.create(AlertService::class.java)
    }

    val foundReportService: FoundReportService by lazy {
        retrofit.create(FoundReportService::class.java)
    }

    val partnerPointService: PartnerPointService by lazy {
        retrofit.create(PartnerPointService::class.java)
    }

    val returnProcessService: ReturnProcessService by lazy {
        retrofit.create(ReturnProcessService::class.java)
    }

    val reviewApiService: ReviewService by lazy {
        retrofit.create(ReviewService::class.java)
    }
}