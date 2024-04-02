package com.example.bondoman.activities

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.R
import com.example.bondoman.lib.SecurePreferences
import com.example.bondoman.repositories.AuthRepository
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var loginButton: Button
    private lateinit var loginRepository: AuthRepository
    private var loadingDialog: Dialog? = null
    private lateinit var layout: LinearLayout;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        layout = findViewById<LinearLayout>(R.id.layout)

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layout.orientation = LinearLayout.HORIZONTAL
        } else {
            layout.orientation = LinearLayout.VERTICAL
        }

        Handler().postDelayed({
            // Transition to the login screen after SPLASH_DURATION
            val cardLogin = findViewById<CardView>(R.id.login_card)
            val logo = findViewById<ImageView>(R.id.imageView)

            val slideUpAnimation = AnimationUtils.loadAnimation(this@LoginActivity, R.anim.slide_up)
            val finalY = cardLogin.y + (cardLogin.height - logo.height) / 2

            val animatorY = ObjectAnimator.ofFloat(logo, "translationY", 0f + logo.height, finalY)
            animatorY.duration = 600

            animatorY.start()
            cardLogin.startAnimation(slideUpAnimation)
            cardLogin.visibility = CardView.VISIBLE
        }, 1000)



        val securePreferences = SecurePreferences(this)
        loginRepository = AuthRepository(securePreferences)
        if(securePreferences.getToken() != null){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener{
            lifecycleScope.launch {
                login(email.text.toString(), password.text.toString())
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = newConfig.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layout.orientation = LinearLayout.HORIZONTAL
        } else {
            layout.orientation = LinearLayout.VERTICAL
        }
    }

    private suspend fun login(email : String, password : String){
        showLoadingDialog()
        val result = loginRepository.login(email, password)
        hideLoadingDialog()
        if(result.isSuccess){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val message = result.exceptionOrNull()?.message ?: "Login failed!"
            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
        }
    }
    private fun showLoadingDialog() {
        loadingDialog = Dialog(this).apply {
            setContentView(R.layout.loading)
            setCancelable(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }

    private fun hideLoadingDialog() {
        loadingDialog?.dismiss()
    }
}