package com.example.myfirstkmpapp.api

import com.example.myfirstkmpapp.data.ZipCodeResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ZipCodeApi {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
    }

    suspend fun getAddressFromZipCode(zipCode: String): ZipCodeResponse {
        val response = client.get("https://zipcloud.ibsnet.co.jp/api/search") {
            url {
                parameters.append("zipcode", zipCode)
            }
        }
        
        // レスポンスのテキストを取得
        val responseText = response.bodyAsText()
        
        // テキストをJSONとしてパース
        return json.decodeFromString<ZipCodeResponse>(responseText)
    }
}
