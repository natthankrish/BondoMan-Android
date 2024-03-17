package com.example.bondoman.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bondoman.databinding.FragmentGrafBinding
import com.example.bondoman.entities.Transaction
import com.example.bondoman.lib.ITransactionGraphAdapter
import com.example.bondoman.lib.TransactionPieChartAdapter
import com.github.mikephil.charting.charts.PieChart

class GrafFragment : Fragment() {
    private var _binding: FragmentGrafBinding? = null
    private val binding get() = _binding!!
    private lateinit var graphAdapter: ITransactionGraphAdapter<PieChart>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val pieChart = binding.pieChart
        graphAdapter = TransactionPieChartAdapter()
        //        TODO: use real data
        val transactions = arrayOf(
            Transaction(1, "Beli seblak", "Pengeluaran", 33000.0f, "Seblak Jeletot Store", "test@email.com"),
            Transaction(2, "Jual baju", "Pemasukan", 50000.0f, "Tokopaedi", "test@email.com")
        )
        val graph = binding.pieChart
        graphAdapter.generateGraph(transactions, pieChart)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}