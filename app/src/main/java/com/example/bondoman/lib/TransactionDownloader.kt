package com.example.bondoman.lib

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.example.bondoman.entities.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class TransactionDownloader {
    suspend fun downloadTransactionAsFile(
        context: Context,
        fileName: String,
        transactions: List<Transaction>,
        mimeType: String,
        transactionFileAdapter: ITransactionFileAdapter) {
        withContext(Dispatchers.IO) {

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            if (uri != null) {
                try {
                    resolver.openOutputStream(uri).use { outputStream ->
                        transactionFileAdapter.save(transactions, "love.xlsx", outputStream!!)
                    }
                    Log.d("File saved", "$fileName is saved successfully")
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}