package br.com.biptag.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Alert(
    val id: Int? = null,

    @SerialName("item_id")
    val itemId: Int,

    val type: String,

    @SerialName("last_seen_lat")
    val lastSeenLat: Double? = null,

    @SerialName("last_seen_lng")
    val lastSeenLng: Double? = null,

    @SerialName("last_seen_address")
    val lastSeenAddress: String? = null,

    @SerialName("incident_date")
    val incidentDate: String? = null,

    @SerialName("radius_km")
    val radiusKm: Double? = null,

    val status: String = "active",

    @SerialName("created_at")
    val createdAt: String? = null,

    val description: String? = null
)