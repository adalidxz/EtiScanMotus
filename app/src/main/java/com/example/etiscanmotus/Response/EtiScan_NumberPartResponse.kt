package com.example.etiscanmotus.Response

data class EtiScan_NumberPartResponse(val ID_EtiScan_Scan: Int,
                                      val ID_Usuario: Int,
                                      val ID_NumberPart: Int,
                                      val ID_EtiScan_NumberPart_F: Int,
                                      val CodeLabel: String,
                                      val CodeMaster: String,
                                      val ID_EtiScan_NumberPart_F_Container: Int,
                                      val Name_Container: String,
                                      val ID_EtiScan_ScanNumbers: Int,
                                      val CodeLabel_Checked: String,
                                      val CodeMaster_Checked: String,
                                      val CodeMaster_Serial: String,
                                      val Date: String,
                                      val Status: Boolean,
                                      val NumeroParte: String)
