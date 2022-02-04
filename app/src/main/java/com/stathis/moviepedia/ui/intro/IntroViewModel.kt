package com.stathis.moviepedia.ui.intro

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class IntroViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    val registerSuccess = MutableLiveData<Pair<Boolean, String>>()
    val loginSuccess = MutableLiveData<Pair<Boolean, String>>()
    val userVerified = MutableLiveData<Boolean>()

    fun verifyEmail(email : String) {
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val isNewUser: Boolean? = it.result?.signInMethods?.isEmpty()
                    if (isNewUser!!) {
                        Log.e("TAG", "Is New User!")
                        userVerified.value = false
                    } else {
                        sendEmail(email)
                    }
                }
            }
    }

    private fun sendEmail(email : String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userVerified.value = true
                }
            }
    }


    fun loginUser(email: String, pass: String) {
        when (validateLoginInfo(email, pass)) {
            true -> {
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        loginSuccess.value = Pair(task.isSuccessful, "AUTH")
                    }
            }
            false -> loginSuccess.value = Pair(false, "AUTH")
        }
    }

    private fun validateLoginInfo(email: String, pass: String) : Boolean {
        if (email.isBlank()) {
            loginSuccess.value = Pair(false, "Please enter your e-mail")
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginSuccess.value = Pair(false, "Please enter a valid e-mail address")
            return false
        }

        if (pass.isEmpty()) {
            loginSuccess.value = Pair(false, "Please enter your password")
            return false
        }

        return true
    }

    fun registerUser(email: String, pass: String, passConfirm: String) {
        when (validateUserInput(email, pass, passConfirm)) {
            true -> {
                //if the 2 passwords match, I am registering him to the RealTime Db
                if (pass == passConfirm) {
                    auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener { task ->
                            registerSuccess.value = Pair(task.isSuccessful, "AUTH")
                        }
                }
            }
            false -> registerSuccess.value = Pair(false, "AUTH Error")
        }
    }

    fun validateUserInput(
        email: String,
        pass: String,
        passConfirm: String
    ): Boolean {
        if (email.isBlank()) {
            registerSuccess.value = Pair(false, "Please enter your e-mail")
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registerSuccess.value = Pair(false, "Please enter a valid e-mail address")
            return false
        }

        if (pass.isEmpty()) {
            registerSuccess.value = Pair(false, "Please enter your password")
            return false
        }

        if (passConfirm.isEmpty()) {
            registerSuccess.value = Pair(false, "Please confirm your password")
            return false
        }
        return true
    }
}