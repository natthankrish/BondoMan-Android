package com.example.bondoman.application

import android.app.Application
import com.example.bondoman.database.room.TransactionDatabase
import com.example.bondoman.repositories.room.RoomTransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TransactionApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { TransactionDatabase.getInstance(this, applicationScope) }
    val repository by lazy { RoomTransactionRepository(database.transactionDao()) }
}