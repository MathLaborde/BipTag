package br.com.biptag.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReturnProcess(
    val id: Int? = null,

    val alertId: Int,

    val foundReportId: Int? = null,

    val returnType: String,

    val partnerPointId: Int? = null,

    val deliveryFee: Double? = null,

    val returnCode: String? = null,

    val status: String = "pending",

    // A etapa atual da logística.
    // Pode ser:
    //      pending (aguardando iniciar),
    //      with_finder (com quem achou),
    //      in_transit (a caminho com o motoboy),
    //      ready_for_pickup (disponível no ponto parceiro)
    //      completed (entregue ao dono).


    val createdAt: String? = null
)