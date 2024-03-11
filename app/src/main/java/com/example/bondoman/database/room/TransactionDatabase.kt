package com.example.bondoman.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bondoman.entities.Transaction
import com.example.bondoman.repositories.room.ITransactionDao

@Database(entities = [Transaction::class], version = 1)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): ITransactionDao
    companion object {
        @Volatile
        private var INSTANCE: TransactionDatabase? = null
        fun getInstance(context: Context): TransactionDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDatabase::class.java, "transaction_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}

