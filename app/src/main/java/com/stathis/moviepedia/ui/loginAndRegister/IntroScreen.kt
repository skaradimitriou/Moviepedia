package com.stathis.moviepedia.ui.loginAndRegister

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.stathis.moviepedia.ui.dashboard.Dashboard
import com.stathis.moviepedia.ui.personalizeAccount.PersonalizeAccount
import com.stathis.moviepedia.R
import com.stathis.moviepedia.databinding.ActivityIntroScreenBinding
import com.stathis.moviepedia.ui.forgotPassword.forgotPass.ForgotPassword
import kotlinx.android.synthetic.main.login_view.view.loginAccBtn
import kotlinx.android.synthetic.main.register_view.view.*

class IntroScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityIntroScreenBinding
    private lateinit var introScreenViewModel: IntroScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        introScreenViewModel = ViewModelProvider(this).get(IntroScreenViewModel::class.java)
        auth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        binding.forgotLoginCredentials.setOnClickListener{
            startActivity(Intent(this, ForgotPassword::class.java))
        }

        binding.loginBtn.setOnClickListener {
            showLoginDialogue()
        }

        binding.registerBtn.setOnClickListener {
            showRegisterDialogue()
        }
    }

    private fun showLoginDialogue() {
        val loginDialog = LayoutInflater.from(this).inflate(R.layout.login_view, null)
        val loginBuilder = AlertDialog.Builder(this)
            .setView(loginDialog).show()
        val loginDial = loginBuilder.show()
        val forgotPass:TextView = loginDialog.findViewById(R.id.forgot_login)
        forgotPass.setOnClickListener {
            startActivity(Intent(this,ForgotPassword::class.java))
        }
        loginDialog.loginAccBtn.setOnClickListener {
            val emailField: TextInputEditText = loginDialog.findViewById(R.id.loginEmailField)
            val passwordField: TextInputEditText = loginDialog.findViewById(R.id.loginPasswordField)

            when (introScreenViewModel.validateLoginCredentials(emailField, passwordField)) {
                true -> {
                    auth.signInWithEmailAndPassword(
                        emailField.text.toString(),
                        passwordField.text.toString()
                    )
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                val user = auth.currentUser
                                updateUI(user)
                            } else {
                                val toast = Toast.makeText(
                                    applicationContext,
                                    "Please enter valid credentials",
                                    Toast.LENGTH_LONG
                                )
                                toast.show()
                                updateUI(null)
                            }
                        }
                }
                false -> {
                    return@setOnClickListener
                }
            }
        }
    }

    private fun showRegisterDialogue() {
        val registerDialogView = LayoutInflater.from(this).inflate(R.layout.register_view, null)
        val registerBuilder = AlertDialog.Builder(this)
            .setView(registerDialogView)
        val registerDialog = registerBuilder.show()
        registerDialogView.registerAccBtn.setOnClickListener {
            val emailField: TextInputEditText = registerDialog.findViewById(R.id.registerEmailField)
            val passwordField: TextInputEditText =
                registerDialog.findViewById(R.id.registerPasswordField)
            val passwordConfigField: TextInputEditText =
                registerDialog.findViewById(R.id.registerPasswordConfirmField)

            when (introScreenViewModel.validateRegisterCredentials(
                emailField,
                passwordField,
                passwordConfigField
            )) {
                true -> {
                    //if the 2 passwords match, I am registering him to the RealTime Db
                    if (passwordField.text.toString() == passwordConfigField.text.toString()) {
                        auth.createUserWithEmailAndPassword(
                            emailField.text.toString(),
                            passwordField.text.toString()
                        )
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    registerDialog.dismiss()
                                    showSuccessAccountDialog()
                                } else {
                                    val toast = Toast.makeText(
                                        applicationContext,
                                        "Please enter valid credentials",
                                        Toast.LENGTH_LONG
                                    )
                                    toast.show()
                                }
                            }
                    }
                }
                false -> {
                    return@setOnClickListener
                }
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            //logging in successful
            startActivity(Intent(this, Dashboard::class.java))
        }
    }

    private fun showSuccessAccountDialog() {
        //Inflate the dialog with custom view
        val successDialog = LayoutInflater.from(this).inflate(R.layout.account_success_view, null)
        //AlertDialogBuilder
        val successBuilder = AlertDialog.Builder(this)
            .setView(successDialog)
        //show dialog
        val successDialogue = successBuilder.show()
        //2 second delay
        Handler().postDelayed({
            successDialogue.dismiss()
            startActivity(Intent(this, PersonalizeAccount::class.java))
        }, 2000)
    }
}