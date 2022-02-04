package com.stathis.moviepedia.ui.forgotPassword.forgotPass

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractActivity
import com.stathis.moviepedia.databinding.ActivityForgotPasswordBinding
import com.stathis.moviepedia.ui.loginAndRegister.IntroScreen
import kotlinx.android.synthetic.main.account_success_view.view.*
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPassword : AbstractActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initLayout() {
        //FIXME: Delete Screen. It is not moved to intro package with all the login/register flow


        viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
    }

    override fun running() {
        send_activation_email.setOnClickListener {
            viewModel.verifyEmail(forgot_email_field.text.toString())
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.userVerified.observe(this, Observer {
            when (it) {
                true -> showSuccessDialog()

                false -> Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun stopped() {
        viewModel.userVerified.removeObservers(this)
    }


    private fun showSuccessDialog() {
        val successDialog = LayoutInflater.from(this).inflate(R.layout.account_success_view, null)
        val successBuilder = AlertDialog.Builder(this)
            .setView(successDialog).show()
        successDialog.redirect_txt.text = "Follow the steps in your e-mail to reset your password"
        Handler().postDelayed({
            successBuilder.dismiss()
            startActivity(Intent(this, IntroScreen::class.java))
            finish()
        }, 4000)
    }
}