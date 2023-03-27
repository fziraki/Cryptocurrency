package com.example.cryptocurrency.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cryptocurrency.domain.model.Crypto

@Entity
data class PinnedCryptoEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val symbol: String,
    val priceInUsdt: String,
    val changePercent: String
){
    fun toCrypto(): Crypto {
        return Crypto(
            id = id,
            name = name,
            symbol = symbol,
            priceInUsdt = priceInUsdt,
            changePercent = changePercent
        )
    }
}