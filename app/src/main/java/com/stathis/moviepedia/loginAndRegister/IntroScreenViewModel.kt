package com.stathis.moviepedia.loginAndRegister

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText

class IntroScreenViewModel : ViewModel() {

    fun validateLoginCredentials(emailField: TextInputEditText,passwordField: TextInputEditText): Boolean {
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

    fun validateRegisterCredentials(emailField: TextInputEditText,passwordField: TextInputEditText,passwordConfigField:TextInputEditText) : Boolean{
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
}