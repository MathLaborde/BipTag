package br.com.biptag.services

import br.com.biptag.model.Alert
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AlertService {
    @GET("api/v1/alerts/with-reports")
    suspend fun getAllAlerts(@Header("Authorization") token: String): List<Alert>

    @GET("api/v1/alerts/{id}")
    suspend fun getAlertById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Alert

    @POST("api/v1/alerts")
    suspend fun createAlert(
        @Header("Authorization") token: String,
        @Body alert: Alert
    ): Alert

    // Nova rota para dar baixa no alerta
    @PUT("api/v1/alerts/{id}/resolve")
    suspend fun resolveAlert(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>
}