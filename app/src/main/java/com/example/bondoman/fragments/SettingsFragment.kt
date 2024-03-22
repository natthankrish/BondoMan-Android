package com.example.bondoman.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.database.TransactionDatabase
import com.example.bondoman.databinding.FragmentSettingsBinding
import com.example.bondoman.entities.Transaction
import com.example.bondoman.lib.ITransactionFileAdapter
import com.example.bondoman.lib.TransactionDownloader
import com.example.bondoman.lib.TransactionExcelAdapter
import com.example.bondoman.repositories.TransactionRepository
import com.example.bondoman.viewModels.TransactionViewModelFactory
import com.example.bondoman.viewModels.TransactionsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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
    private lateinit var transactionDownloader: TransactionDownloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionViewModel.allTransaction.observe(this) {
            transactions = it
        }
        transactionFileAdapter = TransactionExcelAdapter()
        transactionDownloader = TransactionDownloader()
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
        binding.loadingAnimation.isVisible = false
        binding.saveButton.setOnClickListener {
            Log.d("SettingsFragment", "Loading started")
            showLoading()
            val context = requireContext()
            this.lifecycleScope.launch {
                val result = async(Dispatchers.IO) {
                    transactionDownloader.downloadTransactionAsFile(context, "Transactions.xlsx", transactions, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", transactionFileAdapter)
                }
                result.await()
                Log.d("SettingsFragment", "Loading finished")
                hideLoading()
            }
        }
    }

    private fun showLoading() {
        binding.loadingAnimation.isVisible = true
    }

    private fun hideLoading() {
        binding.loadingAnimation.isVisible = false
    }

}