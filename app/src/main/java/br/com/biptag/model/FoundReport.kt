package br.com.biptag.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FoundReport(
    val id: Int? = null,

    val itemId: Int,

    val finderId: String,

    val alertId: Int,

    val foundLat: Double,

    val foundLng: Double,

    val foundAddress: String,

    val foundDate: String,

    val notes: String? = null,

    val isAnonymous: Boolean,

    val createdAt: String? = null
)