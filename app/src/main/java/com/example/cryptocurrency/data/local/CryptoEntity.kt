package com.example.cryptocurrency.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cryptocurrency.domain.model.Crypto

@Entity
data class CryptoEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val nameFa: String,
    val symbol: String,
    val priceInUsdt: String,
    val changePercent: String,
    val usdtVolume: String
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