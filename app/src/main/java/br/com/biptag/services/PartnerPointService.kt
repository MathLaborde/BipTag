package br.com.biptag.services

import br.com.biptag.model.PartnerPoint
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface PartnerPointService {
    @GET("api/v1/partner-points")
    suspend fun getAllPartnerPoints(
        @Header("Authorization") token: String
    ): Response<List<PartnerPoint>>

    @GET("api/v1/partner-points/{id}")
    suspend fun getPartnerPointById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<PartnerPoint>
}