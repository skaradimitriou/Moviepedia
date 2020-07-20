package com.stathis.moviepedia

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.stathis.moviepedia.models.User
import kotlinx.android.synthetic.main.activity_intro_screen.*
import kotlinx.android.synthetic.main.login_view.view.*
import kotlinx.android.synthetic.main.register_view.*
import kotlinx.android.synthetic.main.register_view.view.*
import kotlinx.android.synthetic.main.register_view.view.registerEmailField

class IntroScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_screen)
        auth = FirebaseAuth.getInstance();
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        login_btn.setOnClickListener {
            showLoginDialogue()
        }

        registerBtn.setOnClickListener {
            showRegisterDialogue()
        }
    }

    private fun showLoginDialogue() {
        //Inflate the dialog with custom view
        val loginDialog = LayoutInflater.from(this).inflate(R.layout.login_view, null)
        //AlertDialogBuilder
        val loginBuilder = AlertDialog.Builder(this)
            .setView(loginDialog)
        //show dialog
        loginBuilder.show()
        //login button click of custom layout
        loginDialog.loginAccBtn.setOnClickListener {
            val emailField: TextInputEditText = loginDialog.findViewById(R.id.loginEmailField)
            val passwordField: TextInputEditText = loginDialog.findViewById(R.id.loginPasswordField)

            if (emailField.text.toString().isEmpty()) {
                emailField.error = "Please enter your e-mail"
                emailField.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailField.text.toString()).matches()) {
                emailField.error = "Please enter a valid e-mail address"
                emailField.requestFocus()
                return@setOnClickListener
            }

            if (passwordField.text.toString().isEmpty()) {
                passwordField.error = "Please enter your password"
                passwordField.requestFocus()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(
                emailField.text.toString(),
                passwordField.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        updateUI(user)
                        startActivity(Intent(this, Dashboard::class.java))
                        finish() //kills IntroScreenApp
                    } else {
                        Toast.makeText(
                            baseContext, "Login Failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }
        }
    }

    private fun showRegisterDialogue() {
        //Inflate the dialog with custom view
        val registerDialogView = LayoutInflater.from(this).inflate(R.layout.register_view, null)
        //AlertDialogBuilder
        val registerBuilder = AlertDialog.Builder(this)
            .setView(registerDialogView)
        //show dialog
        val registerDialog = registerBuilder.show()
        registerDialogView.registerAccBtn.setOnClickListener {
            val emailField: TextInputEditText = registerDialog.findViewById(R.id.registerEmailField)
            val passwordField: TextInputEditText =
                registerDialog.findViewById(R.id.registerPasswordField)
            val passwordConfigField: TextInputEditText =
                registerDialog.findViewById(R.id.registerPasswordConfirmField)

            if (emailField.text.toString().isEmpty()) {
                emailField.error = "Please enter your e-mail"
                emailField.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailField.text.toString()).matches()) {
                emailField.error = "Please enter a valid e-mail address"
                emailField.requestFocus()
                return@setOnClickListener
            }

            if (passwordField.text.toString().isEmpty()) {
                passwordField.error = "Please enter your password"
                passwordField.requestFocus()
                return@setOnClickListener
            }

            if (passwordConfigField.text.toString().isEmpty()) {
                passwordConfigField.error = "Please confirm your password"
                passwordConfigField.requestFocus()
                return@setOnClickListener
            }

            if (passwordField.text.toString() == passwordConfigField.text.toString()) {
                auth.createUserWithEmailAndPassword(
                    emailField.text.toString(),
                    passwordField.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            registerDialog.dismiss()
                        }
                    }
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
    }
}