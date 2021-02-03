package com.example.etiscanmotus.Response

import java.time.format.DateTimeFormatter

data class EtiScan_ListScan_Response(
    val ID_EtiScan_Scan: Int,
    val ID_Usuario: Int,
    val Nombre: String,
    val ID_EtiScan_NumberPart_F: Int,
    val NumberPart_F:String,
    val Date: String,
    val Time: String,
    val Date_Start:String?,
    val Date_End:String?,
    val Status:Boolean,
    val Active: Boolean
)
