package br.com.biptag.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item (

    val id: Int? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("user_id")
    val userId: String = "",

    val name: String = "",
    val description: String = "",

    @SerialName("category")
    val category: Int,
    @SerialName("category_data")
    val categoryData: Category? = Category(0, ""),

    val status: String = "Criado",

    @SerialName("nf_key")
    val nfKey: String? = null,
    @SerialName("nf_create_data")
    val nfCreateData: String? = null,
    @SerialName("nf_photo_url")
    val nfPhotoUrl: String? = null,

    @SerialName("image")
    val image: String? = null,

    @SerialName("attach_tag_date")
    val attachTagDate: String? = null,
    @SerialName("tag_id")
    val tagId: String? = null,
)