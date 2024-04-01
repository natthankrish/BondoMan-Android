package com.example.bondoman.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.R
import com.example.bondoman.activities.LoginActivity
import com.example.bondoman.activities.MainActivity
import com.example.bondoman.database.TransactionDatabase
import com.example.bondoman.databinding.FragmentSettingsBinding
import com.example.bondoman.entities.Transaction
import com.example.bondoman.lib.ITransactionFileAdapter
import com.example.bondoman.lib.SecurePreferences
import com.example.bondoman.lib.TransactionDownloader
import com.example.bondoman.lib.TransactionExcelAdapter
import com.example.bondoman.repositories.AuthRepository
import com.example.bondoman.repositories.TransactionRepository
import com.example.bondoman.viewModels.TransactionViewModelFactory
import com.example.bondoman.viewModels.TransactionsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    private lateinit var authRepository: AuthRepository
    private lateinit var transactions: List<Transaction>
    private lateinit var transactionFileAdapter: ITransactionFileAdapter
    private lateinit var transactionDownloader: TransactionDownloader
//    private var authRepository : AuthRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionViewModel.allTransaction.observe(this) {
            transactions = it
        }
        transactionFileAdapter = TransactionExcelAdapter()
        transactionDownloader = TransactionDownloader()
        authRepository =  AuthRepository(SecurePreferences(requireContext()))
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
            binding.saveButton.isClickable = false
            showLoading()
            val context = requireContext()
            val fileName = createFileName(transactions)
            this.lifecycleScope.launch {
                val result = async(Dispatchers.IO) {
                    transactionDownloader.downloadTransactionAsFile(
                        context,
                        fileName,
                        transactions,
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                        transactionFileAdapter
                    )
                }
                result.await()
                Log.d("SettingsFragment", "Loading finished")
                hideLoading()
                showSnackbar("Your transactions have been exported inside Download file")
                binding.saveButton.isClickable = true
            }
        }
        binding.logoutButton.setOnClickListener{
            lifecycleScope.launch {
                showLoading()
                logout()
                hideLoading()
            }
        }
    }

    private suspend fun logout() {
        val response = authRepository.logout()
        if (response.isSuccess) {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }else{
            withContext(Dispatchers.Main) {
                val layoutInflater = LayoutInflater.from(requireContext())
                val view = layoutInflater.inflate(R.layout.custom_toast, null)
                val textView = view.findViewById<TextView>(R.id.customToastText)
                textView.text = "Logout failed, please try again!"
                with (Toast(requireContext())) {
                    duration = Toast.LENGTH_LONG
                    setView(view)
                    show()
                }
            }
        }
    }
    private fun showLoading() {
        binding.loadingAnimation.isVisible = true
    }

    private fun hideLoading() {
        binding.loadingAnimation.isVisible = false
    }

    private fun createFileName(transactions: List<Transaction>): String {
        val dateFormat = SimpleDateFormat("dd MM yyyy HH:mm:ss:SSS", Locale.getDefault())
        val currentTime = dateFormat.format(Date())

        val userEmail = transactions.getOrNull(0)?.userEmail ?: "UnknownUser"
        val userName = userEmail.split("@").firstOrNull() ?: "UnknownUser"
        val fileName = "$currentTime $userName Transaction Summary"

        return "$fileName.xlsx"
    }

    private fun showSnackbar(message: String) {
        Snackbar
            .make(binding.snackbarContainer, message, Snackbar.LENGTH_INDEFINITE)
            .setAction("OK") {}
            .show()

    }

}