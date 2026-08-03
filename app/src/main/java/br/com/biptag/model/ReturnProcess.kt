package br.com.biptag.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReturnProcess(
    val id: Int? = null,
    @SerialName("alert_id")
    val alertId: Int,
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

    // A etapa atual da logística.
    // Pode ser:
    //      pending (aguardando iniciar),
    //      with_finder (com quem achou),
    //      in_transit (a caminho com o motoboy),
    //      ready_for_pickup (disponível no ponto parceiro)
    //      completed (entregue ao dono).

    @SerialName("created_at")
    val createdAt: String? = null
)