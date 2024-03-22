package com.example.bondoman.fragments

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.R
import com.example.bondoman.database.TransactionDatabase
import com.example.bondoman.databinding.FragmentSettingsBinding
import com.example.bondoman.entities.Transaction
import com.example.bondoman.lib.ITransactionFileAdapter
import com.example.bondoman.lib.TransactionExcelAdapter
import com.example.bondoman.repositories.TransactionRepository
import com.example.bondoman.viewModels.TransactionViewModelFactory
import com.example.bondoman.viewModels.TransactionsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val transactionViewModel: TransactionsViewModel by viewModels {
        TransactionViewModelFactory(
            TransactionRepository(
                TransactionDatabase.getInstance(requireContext(), CoroutineScope(
                    SupervisorJob()
                )
            ).transactionDao())
        )
    }
    private lateinit var transactions: List<Transaction>
    private lateinit var transactionFileAdapter: ITransactionFileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionViewModel.allTransaction.observe(this) {
            transactions = it
        }
        transactionFileAdapter = TransactionExcelAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveButton.setOnClickListener {
            Log.d("SettingsFragment", "Loading started")

            val context = requireContext()
            this.lifecycleScope.launch(Dispatchers.IO) {
                saveFileUsingMediaStore(context, "Transactions.xlsx", transactions, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", transactionFileAdapter)
                Log.d("SettingsFragment", "Loading finished")
            }
        }
    }

    private suspend fun saveFileUsingMediaStore(
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