package com.example.bondoman.interfaces

import android.icu.text.CaseMap.Title
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
    @Query("SELECT * FROM transactions WHERE userEmail = :userEmail ORDER BY date DESC")
    fun getAll(userEmail : String): Flow<List<Transaction>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transaction: Transaction)
    @Query("UPDATE transactions SET amount = :amount, title = :name, location = :location WHERE id = :id")
    suspend fun update(id: Int, name:String, amount: Float, location: String)
    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun delete(id: Int)
    @Query("DELETE FROM transactions")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM transactions")
    suspend fun getCount() : Int
}