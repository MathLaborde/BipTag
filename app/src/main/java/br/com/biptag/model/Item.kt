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

    @SerialName("category")
    val category: Int,

    @SerialName("category_data")
    val categoryData: Category? = Category(0, ""),

    val status: String? = "created",

    val nfKey: String? = null,

    val nfCreateData: String? = null,

    val nfPhotoUrl: String? = null,

    val image: String? = null,

    val attachTagDate: String? = null,

    val tagId: String? = null,
)