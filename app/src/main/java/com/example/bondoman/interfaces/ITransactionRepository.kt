package com.example.bondoman.interfaces

import androidx.lifecycle.LiveData
import com.example.bondoman.entities.Transaction


interface ITransactionRepository {
    fun getAll(userEmail: String): LiveData<List<Transaction>>
    suspend fun insert(transaction: Transaction)
    suspend fun update(transaction: Transaction)
    suspend fun delete(transaction: Transaction)
}