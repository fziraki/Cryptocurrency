package com.example.cryptocurrency.domain.model


data class Crypto(
    val id: Int,
    val name: String,
    val nameFa: String,
    val symbol: String,
    val priceInUsdt: String,
    val changePercent: String,
    val usdtVolume: String
)