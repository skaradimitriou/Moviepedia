package com.stathis.moviepedia.ui.loginAndRegister

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.stathis.moviepedia.R
import com.stathis.moviepedia.ui.forgotPassword.forgotPass.ForgotPassword
import com.stathis.moviepedia.ui.personalizeAccount.PersonalizeAccount
import kotlinx.android.synthetic.main.login_view.view.*
import kotlinx.android.synthetic.main.register_view.view.*

class IntroScreenViewModel : ViewModel() {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    val loginSuccessful = MutableLiveData<FirebaseUser>()
    val loginFailed = MutableLiveData<Boolean>()

    val registerSuccessful = MutableLiveData<Boolean>()
    val forgotPasswordClicked = MutableLiveData<Boolean>()

    fun showLoginDialogue(context: Context) {
        val loginDialog = LayoutInflater.from(context).inflate(R.layout.login_view, null)
        val loginBuilder = AlertDialog.Builder(context)
            .setView(loginDialog).show()

        loginDialog.loginAccBtn.setOnClickListener {
            val emailField: TextInputEditText = loginDialog.findViewById(R.id.loginEmailField)
            val passwordField: TextInputEditText = loginDialog.findViewById(R.id.loginPasswordField)

            when (validateLoginCredentials(emailField, passwordField)) {
                true -> {
                    auth.signInWithEmailAndPassword(
                        emailField.text.toString(),
                        passwordField.text.toString()
                    )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                loginSuccessful.value = auth.currentUser
                                loginFailed.value = false
                            } else {
                                loginFailed.value = true
                                loginSuccessful.value = null
                            }
                        }
                }
                false -> {
                    return@setOnClickListener
                }
            }
        }
    }

    fun showRegisterDialogue(context: Context) {
        val registerDialogView = LayoutInflater.from(context).inflate(R.layout.register_view, null)
        val registerBuilder = AlertDialog.Builder(context)
            .setView(registerDialogView).show()
        registerDialogView.registerAccBtn.setOnClickListener {
            val emailField: TextInputEditText =
                registerBuilder.findViewById(R.id.registerEmailField)
            val passwordField: TextInputEditText =
                registerBuilder.findViewById(R.id.registerPasswordField)
            val passwordConfigField: TextInputEditText =
                registerBuilder.findViewById(R.id.registerPasswordConfirmField)

            when (validateRegisterCredentials(
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
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    registerBuilder.dismiss()
                                    showSuccessAccountDialog(context)
                                } else {
                                    registerSuccessful.value = false
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

    fun validateLoginCredentials(
        emailField: TextInputEditText,
        passwordField: TextInputEditText
    ): Boolean {
        if (emailField.text.toString().isEmpty()) {
            emailField.error = "Please enter your e-mail"
            emailField.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailField.text.toString()).matches()) {
            emailField.error = "Please enter a valid e-mail address"
            emailField.requestFocus()
            return false
        }

        if (passwordField.text.toString().isEmpty()) {
            passwordField.error = "Please enter your password"
            passwordField.requestFocus()
            return false
        }
        return true
    }

    fun validateRegisterCredentials(
        emailField: TextInputEditText,
        passwordField: TextInputEditText,
        passwordConfigField: TextInputEditText
    ): Boolean {
        if (emailField.text.toString().isEmpty()) {
            emailField.error = "Please enter your e-mail"
            emailField.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailField.text.toString()).matches()) {
            emailField.error = "Please enter a valid e-mail address"
            emailField.requestFocus()
            return false
        }

        if (passwordField.text.toString().isEmpty()) {
            passwordField.error = "Please enter your password"
            passwordField.requestFocus()
            return false
        }

        if (passwordConfigField.text.toString().isEmpty()) {
            passwordConfigField.error = "Please confirm your password"
            passwordConfigField.requestFocus()
            return false
        }
        return true
    }

    fun showSuccessAccountDialog(context: Context) {
        val successDialog =
            LayoutInflater.from(context).inflate(R.layout.account_success_view, null)
        val successBuilder = AlertDialog.Builder(context)
            .setView(successDialog).show()
        registerSuccessful.value = true

    }
}