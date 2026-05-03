package br.com.biptag.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Inventory (

    val id: Int? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("user_id")
    val userId: String = "",

    val name: String = "",
    val description: String = "",
    val category: String = "",
    val status: String = "Created", // Created, Verified, Stolen

    @SerializedName("fiscal_note")
    val fiscalNote: String? = null,

    @SerializedName("image")
    val image: String? = null,
)