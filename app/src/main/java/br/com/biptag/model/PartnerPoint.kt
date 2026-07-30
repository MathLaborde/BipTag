package br.com.biptag.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartnerPoint(
    val id: Int? = null,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    @SerialName("working_hours")
    val workingHours: String,
    @SerialName("created_at")
    val createdAt: String? = null
)