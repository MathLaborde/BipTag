package br.com.biptag.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item (

    val id: Int? = null,

    val createdAt: String? = null,

    val userId: String = "",

    val name: String = "",

    val description: String = "",

    val category: Int,

    val categoryData: Category? = null,

    val status: String? = "CREATED",

    val nfKey: String? = null,

    val nfCreateData: String? = null,

    val nfPhotoUrl: String? = null,

    val image: String? = null,

    val attachTagDate: String? = null,

    val tagId: String? = null,
)