package com.stathis.moviepedia.ui.loginAndRegister

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.stathis.moviepedia.databinding.ActivityIntroScreenBinding
import com.stathis.moviepedia.ui.dashboard.Dashboard
import com.stathis.moviepedia.ui.forgotPassword.forgotPass.ForgotPassword
import com.stathis.moviepedia.ui.personalizeAccount.PersonalizeAccount

class IntroScreen : AppCompatActivity() {

    private lateinit var binding: ActivityIntroScreenBinding
    private lateinit var viewModel: IntroScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(IntroScreenViewModel::class.java)
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = viewModel.auth.currentUser
        updateUI(currentUser)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        //FIXME: Delete Screen. It is not moved to intro package with all the login/register flow

        binding.forgotLoginCredentials.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }

        binding.loginBtn.setOnClickListener {
            viewModel.showLoginDialogue(this)
        }

        binding.registerBtn.setOnClickListener {
            viewModel.showRegisterDialogue(this)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.forgotPasswordClicked.observe(this, Observer {
            when (it) {
                true -> startActivity(Intent(this, ForgotPassword::class.java))
            }
        })

        viewModel.loginSuccessful.observe(this, Observer {
            updateUI(it)
        })

        viewModel.loginFailed.observe(this, Observer {
            when (it) {
                true -> Toast.makeText(this, "Please enter valid credentials", Toast.LENGTH_LONG)
                    .show()
            }
        })

        viewModel.registerSuccessful.observe(this, Observer {
            when (it) {
                true -> {
                    startActivity(Intent(this, PersonalizeAccount::class.java))
                }
                false -> {
                    Toast.makeText(this, "Please enter valid credentials", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, Dashboard::class.java))
        }
    }
}