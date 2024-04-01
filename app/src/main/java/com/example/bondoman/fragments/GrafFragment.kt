package com.example.bondoman.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bondoman.database.TransactionDatabase
import com.example.bondoman.databinding.FragmentGrafBinding
import com.example.bondoman.entities.Transaction
import com.example.bondoman.lib.ITransactionGraphAdapter
import com.example.bondoman.lib.SecurePreferences
import com.example.bondoman.lib.TransactionPieChartAdapter
import com.example.bondoman.repositories.TransactionRepository
import com.example.bondoman.viewModels.TransactionViewModelFactory
import com.example.bondoman.viewModels.TransactionsViewModel
import com.github.mikephil.charting.charts.PieChart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class GrafFragment : Fragment() {
    private var _binding: FragmentGrafBinding? = null
    private val binding get() = _binding!!
    private lateinit var graphAdapter: ITransactionGraphAdapter<PieChart>
    private lateinit var securePreferences: SecurePreferences
    private val transactionViewModel: TransactionsViewModel by viewModels {
        TransactionViewModelFactory(
            TransactionRepository(
                TransactionDatabase.getInstance(requireContext(), CoroutineScope(
                    SupervisorJob()
                )
                ).transactionDao(), securePreferences)
        )
    }
    private lateinit var transactions: List<Transaction>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        graphAdapter = TransactionPieChartAdapter()
        securePreferences = SecurePreferences(requireContext())
        Log.i("EMAIL", securePreferences.getEmail() ?: "KOSONG")
        transactionViewModel.allTransaction.observe(this) {
            transactions = it
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGrafBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val pieChart = binding.pieChart
        graphAdapter.generateGraph(transactions, pieChart)
        for (transaction in transactions) {
            Log.d("GrafFragment", transaction.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}