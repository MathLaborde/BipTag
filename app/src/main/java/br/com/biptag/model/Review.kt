package br.com.biptag.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Review (
    val id: Int? = null,
    @SerialName("return_process_id")
    val returnProcessId: Int? = null,
    @SerialName("reviewer_id")
    val reviewerId: String,
    @SerialName("rating")
    val rating: Int? = null,
    @SerialName("comment")
    val comment: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
){
}