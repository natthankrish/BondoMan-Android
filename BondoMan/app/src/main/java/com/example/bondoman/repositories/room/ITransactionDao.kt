package com.example.bondoman.repositories.room

import androidx.room.Dao
import androidx.room.Query
import com.example.bondoman.entities.Transaction

@Dao
interface ITransactionDao {
    @Query("SELECT * FROM transactions WHERE userEmail = :userEmail")
    suspend fun getAll(userEmail: String): Array<Transaction>
    suspend fun insert(transaction: Transaction)
    suspend fun update(transaction: Transaction)
    suspend fun delete(transaction: Transaction)
}