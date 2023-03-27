package com.example.cryptocurrency.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CryptoDao {

    @Query("SELECT * FROM CryptoEntity")
    suspend fun getCryptos(): List<CryptoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCryptos(cryptos: List<CryptoEntity>)
}