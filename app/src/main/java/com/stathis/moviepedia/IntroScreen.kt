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
import kotlinx.android.synthetic.main.login_view.view.*
import kotlinx.android.synthetic.main.register_view.*
import kotlinx.android.synthetic.main.register_view.view.*
import kotlinx.android.synthetic.main.register_view.view.registerEmailField

class IntroScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var loginBtn: Button
    private lateinit var registerBtn: TextView

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
        loginBtn = findViewById(R.id.login_btn)
        registerBtn = findViewById(R.id.registerBtn)

        loginBtn.setOnClickListener {
            showLoginDialogue()
        }
        registerBtn.setOnClickListener {
            showRegisterDialogue()
        }
    }

    private fun showLoginDialogue() {
        //Inflate the dialog with custom view
        val LoginDialogView = LayoutInflater.from(this).inflate(R.layout.login_view, null)
        //AlertDialogBuilder
        val loginBuilder = AlertDialog.Builder(this)
            .setView(LoginDialogView)
        //show dialog
        val mAlertDialog = loginBuilder.show()
        //login button click of custom layout
        LoginDialogView.loginAccBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            val register = Intent(this, SplashScreen::class.java)
            startActivity(register)
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
            val passwordConfigfield: TextInputEditText =
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

            if (passwordConfigfield.text.toString().isEmpty()) {
                passwordConfigfield.error = "Please confirm your password"
                passwordConfigfield.requestFocus()
                return@setOnClickListener
            }

            if (passwordField.text.toString() != passwordConfigfield.text.toString()) {
                auth.createUserWithEmailAndPassword(
                    emailField.text.toString(),
                    passwordField.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val goToHomePage = Intent(this, SplashScreen::class.java)
                            startActivity(goToHomePage)
                            finish() //kills the intro Screen
                        }
                    }
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {

    }
}

