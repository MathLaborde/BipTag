package br.com.biptag.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Review (
    val id: Int? = null,

    val returnProcessId: Int? = null,

    val reviewerId: String,

    val rating: Int? = null,

    val comment: String? = null,

    val createdAt: String? = null
){
}