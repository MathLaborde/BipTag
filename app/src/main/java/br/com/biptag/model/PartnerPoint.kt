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

    val workingHours: String,

    val createdAt: String? = null
)