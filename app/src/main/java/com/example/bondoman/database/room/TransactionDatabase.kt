package com.example.bondoman.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bondoman.adapter.DateAdapter
import com.example.bondoman.entities.Transaction
import com.example.bondoman.interfaces.ITransactionDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date

@Database(entities = [Transaction::class], version = 1, exportSchema = false)
@TypeConverters(DateAdapter::class)
public abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): ITransactionDao

    private class TransactionDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val dao = database.transactionDao()

                    // Delete all content here.
                    dao.deleteAll()

                    // Add sample words.
                    val transaction = Transaction(
                        id = 1,
                        title = "Example Transaction",
                        category = "Expense",
                        amount = 100.0f,
                        location = "Example Location",
                        date = Date(),
                        userEmail = "example@example.com"
                    )
                    dao.insert(transaction)
                }
            }
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: TransactionDatabase? = null
        fun getInstance(context: Context, scope: CoroutineScope): TransactionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, TransactionDatabase::class.java, "transaction_db").addCallback(TransactionDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

