package com.example.bondoman.repositories.room

import androidx.room.Dao
import androidx.room.Query
import com.example.bondoman.entities.Transaction

@Dao
interface ITransactionDao {
    @Query("SELECT * FROM transactions WHERE userEmail = :userEmail")
    fun getAll(userEmail: String): Array<Transaction>
    fun insert(transaction: Transaction)
    fun update(transaction: Transaction)
    fun delete(transaction: Transaction)
}