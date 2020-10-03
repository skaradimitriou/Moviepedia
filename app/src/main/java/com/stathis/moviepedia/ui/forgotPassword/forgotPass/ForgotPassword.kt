package com.stathis.moviepedia.ui.forgotPassword.forgotPass

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.stathis.moviepedia.databinding.ActivityForgotPasswordBinding
import com.stathis.moviepedia.ui.loginAndRegister.IntroScreen
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPassword : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var viewModel: ForgotPasswordViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        send_activation_email.setOnClickListener {
            verifyEmail()


        }
    }

    private fun verifyEmail() {
        auth.fetchSignInMethodsForEmail(forgot_email_field.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val isNewUser: Boolean? = it.result?.signInMethods?.isEmpty()
                    if (isNewUser!!) {
                        Log.e("TAG", "Is New User!");
                    } else {
                        sendEmail()
                    }
                }
            }
    }

    private fun sendEmail() {
        auth.sendPasswordResetEmail(forgot_email_field.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //success dialog
                    startActivity(Intent(this, IntroScreen::class.java))
                }
            }
    }
}