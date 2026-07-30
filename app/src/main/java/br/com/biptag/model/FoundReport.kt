package br.com.biptag.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FoundReport(
    val id: Int? = null,

    @SerialName("item_id")
    val itemId: Int,

    @SerialName("finder_id")
    val finderId: String,

    @SerialName("found_lat")
    val foundLat: Double,

    @SerialName("found_lng")
    val foundLng: Double,

    @SerialName("found_address")
    val foundAddress: String,

    @SerialName("found_date")
    val foundDate: String,

    val notes: String? = null,

    @SerialName("is_anonymous")
    val isAnonymous: Boolean,

    @SerialName("created_at")
    val createdAt: String? = null
)