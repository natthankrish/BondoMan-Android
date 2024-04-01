package com.example.bondoman.activities

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.R
import com.example.bondoman.apiServices.IAuthService
import com.example.bondoman.lib.SecurePreferences
import com.example.bondoman.repositories.LoginRepository
import com.example.bondoman.retrofits.Retro
import kotlinx.coroutines.launch
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var loginButton: Button
    private lateinit var loginRepository: LoginRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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


        val authService = Retro().getRetroClientInstance().create(IAuthService::class.java)
        val securePreferences = SecurePreferences(this)

        loginRepository = LoginRepository(authService, securePreferences)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener{
            lifecycleScope.launch {
                val result = loginRepository.login(email.text.toString(), password.text.toString())
                if(result.isSuccess){
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    val message = result.exceptionOrNull()?.message ?: "Login failed!"
                    Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}