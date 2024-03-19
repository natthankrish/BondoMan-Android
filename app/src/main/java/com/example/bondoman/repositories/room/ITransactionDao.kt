package com.example.bondoman.repositories.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bondoman.entities.Transaction

@Dao
interface ITransactionDao {
    @Query("SELECT * FROM transactions WHERE userEmail = :userEmail")
    suspend fun getAll(userEmail: String): Array<Transaction>
    @Insert
    suspend fun insert(transaction: Transaction)
    @Update
    suspend fun update(transaction: Transaction)
    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()
}