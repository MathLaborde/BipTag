package br.com.biptag.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReturnProcess(
    val id: Int? = null,
    @SerialName("item_id")
    val itemId: Int,
    @SerialName("found_report_id")
    val foundReportId: Int? = null,
    @SerialName("return_type")
    val returnType: String,
    @SerialName("partner_point_id")
    val partnerPointId: Int? = null,
    @SerialName("delivery_fee")
    val deliveryFee: Double? = null,
    @SerialName("return_code")
    val returnCode: String? = null,
    val status: String = "pending",
    @SerialName("created_at")
    val createdAt: String? = null
)