package com.example.cryptocurrency.data.remote

import com.example.cryptocurrency.data.remote.dto.CryptoDto
import retrofit2.http.GET

interface CryptoApi {

    @GET("r/plots/currency_prices/")
    suspend fun getCryptoList(): List<CryptoDto>
}