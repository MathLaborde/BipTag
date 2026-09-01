package br.com.biptag.services

import br.com.biptag.model.ReturnProcess
import retrofit2.Response
import retrofit2.http.*

interface ReturnProcessService {
    @GET("api/v1/return-processes")
    suspend fun getAllReturnProcesses(
        @Header("Authorization") token: String
    ): Response<List<ReturnProcess>>

    @POST("api/v1/return-processes")
    suspend fun createReturnProcess(
        @Header("Authorization") token: String,
        @Body process: ReturnProcess
    ): Response<ReturnProcess>

    @PUT("api/v1/return-processes/{id}")
    suspend fun updateReturnProcess(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body process: ReturnProcess
    ): Response<ReturnProcess>
    @GET("api/v1/return-processes/{id}")
    suspend fun getReturnProcessById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<ReturnProcess>
}