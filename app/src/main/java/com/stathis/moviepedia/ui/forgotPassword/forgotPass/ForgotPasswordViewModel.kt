package com.stathis.moviepedia.ui.forgotPassword.forgotPass

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordViewModel : ViewModel() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
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
}