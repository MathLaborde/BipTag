package br.com.biptag.services

import br.com.biptag.model.FoundReport
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface FoundReportService {
    @POST("api/v1/found-reports")
    suspend fun createFoundReport(
        @Header("Authorization") token: String,
        @Body report: FoundReport
    ): Response<FoundReport>

    @GET("api/v1/found-reports/{id}")
    suspend fun getFoundReportById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<FoundReport>

    @GET("api/v1/found-reports/alert/{alertId}")
    suspend fun getFoundReportByAlertId(
        @Header("Authorization") token: String,
        @Path("alertId") alertId: Int
    ): Response<FoundReport>
}