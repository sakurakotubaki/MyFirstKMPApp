package com.example.myfirstkmpapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ZipCodeResponse(
    @SerialName("results")
    val results: List<AddressData>? = null,
    @SerialName("status")
    val status: Int = 0,
    @SerialName("message")
    val message: String? = null
)

@Serializable
data class AddressData(
    @SerialName("address1")
    val prefecture: String = "",
    @SerialName("address2")
    val city: String = "",
    @SerialName("address3")
    val town: String = "",
    @SerialName("kana1")
    val prefectureKana: String = "",
    @SerialName("kana2")
    val cityKana: String = "",
    @SerialName("kana3")
    val townKana: String = "",
    @SerialName("prefcode")
    val prefCode: String = "",
    @SerialName("zipcode")
    val zipcode: String = ""
)
