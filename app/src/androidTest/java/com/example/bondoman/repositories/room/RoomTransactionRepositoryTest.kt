package com.example.bondoman.repositories.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.bondoman.database.room.TransactionDatabase
import com.example.bondoman.entities.Transaction
import com.example.bondoman.repositories.ITransactionRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@SmallTest
class RoomTransactionRepositoryTest {
    private lateinit var database: TransactionDatabase
    private lateinit var transactionDao: ITransactionDao
    private lateinit var transactionRepository: ITransactionRepository
    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TransactionDatabase::class.java
        ).allowMainThreadQueries().build()

        transactionDao = database.transactionDao()
        transactionRepository = RoomTransactionRepository(transactionDao)
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insert() = runBlocking {
        transactionRepository.deleteAll()
        val transactionToInsert = Transaction(
            1,
            "Beli seblak",
            "Pengeluaran",
            20000f,
            "Warung teteh",
            "hahaha@email.com"
        )
        transactionRepository.insert(transactionToInsert)

        val transactions = transactionRepository.getAll("hahaha@email.com")
        assert(transactions.isNotEmpty())

        val transactionInDb = transactions[0]

        assert(
            transactionToInsert.id == transactionInDb.id &&
            transactionToInsert.title == transactionInDb.title &&
            transactionToInsert.category == transactionInDb.category &&
            transactionToInsert.amount == transactionInDb.amount &&
            transactionToInsert.location == transactionInDb.location &&
            transactionToInsert.userEmail == transactionInDb.userEmail
        )
        transactionRepository.deleteAll()
    }
}