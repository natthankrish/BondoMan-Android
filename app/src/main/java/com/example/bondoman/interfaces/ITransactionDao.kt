package com.example.bondoman.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bondoman.entities.Transaction
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface ITransactionDao {
    @Query("SELECT * FROM transactions WHERE userEmail = :userEmail OR userEmail != :userEmail ORDER BY date DESC")
    fun getAll(userEmail : String): Flow<List<Transaction>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transaction: Transaction)
    @Update
    suspend fun update(transaction: Transaction)
    @Delete
    suspend fun delete(transaction: Transaction)
    @Query("DELETE FROM transactions")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM transactions")
    suspend fun getCount() : Int
}