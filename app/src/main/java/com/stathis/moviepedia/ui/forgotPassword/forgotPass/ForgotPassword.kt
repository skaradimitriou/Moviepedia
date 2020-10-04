package com.stathis.moviepedia.ui.forgotPassword.forgotPass

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.stathis.moviepedia.R
import com.stathis.moviepedia.databinding.ActivityForgotPasswordBinding
import com.stathis.moviepedia.ui.loginAndRegister.IntroScreen
import kotlinx.android.synthetic.main.account_success_view.view.*
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
                    showSuccessDialog()
                }
            }
    }

    private fun showSuccessDialog() {
        //Inflate the dialog with custom view
        val successDialog = LayoutInflater.from(this).inflate(R.layout.account_success_view, null)
        //AlertDialogBuilder
        val successBuilder = AlertDialog.Builder(this)
            .setView(successDialog)
        successDialog.redirect_txt.text = "Follow the steps in your e-mail to reset your password"
        //show dialog
        val successDialogue = successBuilder.show()
        //2 second delay
        Handler().postDelayed({
            successDialogue.dismiss()
            startActivity(Intent(this, IntroScreen::class.java))
            finish()
        }, 4000)
    }
}