package com.example.cryptocurrency.data.remote.dto


import com.example.cryptocurrency.domain.model.Crypto
import com.google.gson.annotations.SerializedName

data class CryptoDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("name_fa")
    val nameFa: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("price_in_usdt")
    val priceInUsdt: String,
    @SerializedName("change_percent")
    val changePercent: String,
    @SerializedName("usdt_volume")
    val usdtVolume: String,

    @SerializedName("created")
    val created: String,
    @SerializedName("is_swappable")
    val isSwappable: Boolean,
    @SerializedName("volume")
    val volume: String
){
    fun toCrypto(): Crypto {
        return Crypto(
            id = id,
            name = name,
            nameFa = nameFa,
            symbol = symbol,
            priceInUsdt = priceInUsdt,
            changePercent = changePercent,
            usdtVolume = usdtVolume
        )
    }
}