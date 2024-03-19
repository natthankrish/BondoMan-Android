package com.example.bondoman.repositories.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.bondoman.entities.Transaction
import com.example.bondoman.interfaces.ITransactionDao
import com.example.bondoman.interfaces.ITransactionRepository
import kotlinx.coroutines.flow.Flow

class RoomTransactionRepository(private val transactionDao: ITransactionDao) {
    val allTransaction : Flow<List<Transaction>> = transactionDao.getAll("testing")

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }

    suspend fun update(transaction: Transaction)
        = transactionDao.update(transaction)

    suspend fun delete(transaction: Transaction)
        = transactionDao.delete(transaction)
}