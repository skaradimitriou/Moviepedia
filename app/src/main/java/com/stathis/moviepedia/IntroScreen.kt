package com.stathis.moviepedia

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_intro_screen.*
import kotlinx.android.synthetic.main.login_view.view.loginAccBtn
import kotlinx.android.synthetic.main.register_view.view.*

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
        val loginDial = loginBuilder.show()
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
//                        startActivity(Intent(this, Dashboard::class.java))
//                        finish() //kills IntroScreenApp
                        loginDial.dismiss()
                        showSuccessLoginDialog()
                    } else {
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
                            showSuccessAccountDialog()
                        } else {

                        }
                    }
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {

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
            startActivity(Intent(this, GenresInfoScreen::class.java))
        }, 2000)
    }

    private fun showSuccessLoginDialog() {
        //Inflate the dialog with custom view
        val successDialog = LayoutInflater.from(this).inflate(R.layout.account_success_view, null)
        //AlertDialogBuilder
        val successBuilder = AlertDialog.Builder(this)
            .setView(successDialog)
        val text: TextView = successDialog.findViewById(R.id.redirect_txt)
        text.text = "Login successful"
        //show dialog
        val successDialogue = successBuilder.show()
        //2 second delay
        Handler().postDelayed({
            successDialogue.dismiss()
            startActivity(Intent(this, Dashboard::class.java))
            finish() //kills IntroScreenApp
        }, 2000)
    }
}