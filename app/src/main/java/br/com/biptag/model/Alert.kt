package br.com.biptag.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Alert(
    val id: Int? = null,

    @SerialName("item_id")
    val itemId: Int,

    val type: String, // "lost" ou "stolen"
    val latitude: Double? = null,
    val longitude: Double? = null,
    val status: String = "active"
)