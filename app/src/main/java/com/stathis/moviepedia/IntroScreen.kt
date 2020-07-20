package com.stathis.moviepedia

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.login_view.*
import kotlinx.android.synthetic.main.login_view.view.*
import kotlinx.android.synthetic.main.register_view.view.*

class IntroScreen : AppCompatActivity() {

    private lateinit var loginBtn: Button
    private lateinit var registerBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_screen)
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
        val RegisterDialogView = LayoutInflater.from(this).inflate(R.layout.register_view, null)
        //AlertDialogBuilder
        val registerBuilder = AlertDialog.Builder(this)
            .setView(RegisterDialogView)
        //show dialog
        val mAlertDialog = registerBuilder.show()
        //login button click of custom layout
        RegisterDialogView.registerAccBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            val register = Intent(this, SplashScreen::class.java)
            startActivity(register)
        }
    }
}
