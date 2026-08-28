package br.com.biptag.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Alert(
    val id: Int? = null,

    val itemId: Int,

    val itemData: Item? = null,

    val type: String,

    val lastSeenLat: Double? = null,

    val lastSeenLng: Double? = null,

    val lastSeenAddress: String? = null,

    val incidentDate: String? = null,

    val radiusKm: Double? = null,

    val status: String = "active",

    val createdAt: String? = null,

    val description: String? = null
)