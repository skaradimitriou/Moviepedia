package com.stathis.moviepedia.ui.intro.fragments


import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractBindingFragment
import com.stathis.moviepedia.databinding.FragmentLoginBinding
import com.stathis.moviepedia.ui.dashboard.Dashboard
import com.stathis.moviepedia.ui.forgotPassword.forgotPass.ForgotPassword
import com.stathis.moviepedia.ui.intro.IntroViewModel


class LoginFragment : AbstractBindingFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private lateinit var viewModel : IntroViewModel

    override fun init() {
        viewModel = ViewModelProvider(requireActivity()).get(IntroViewModel::class.java)
    }

    override fun startOps() {
        binding.loginAccBtn.setOnClickListener {
            val email = binding.loginEmailField.text.toString()
            val pass = binding.loginPasswordField.text.toString()

            viewModel.loginUser(email,pass)
        }

        binding.forgotLogin.setOnClickListener {
            startActivity(Intent(requireContext(),ForgotPassword::class.java))
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner, Observer {
            when(it.first){
                true -> startActivity(Intent(requireActivity(),Dashboard::class.java))
                false -> handleUiErrors(it.second)
            }
        })
    }

    override fun stopOps() {
        viewModel.loginSuccess.removeObservers(viewLifecycleOwner)
    }

    private fun handleUiErrors(errorMsg: String) = when {
        errorMsg.contains("valid") -> {
            binding.loginEmailField.error = errorMsg
            binding.loginEmailField.requestFocus()
        }
        errorMsg.contains("e-mail") -> {
            binding.loginEmailField.error = errorMsg
            binding.loginEmailField.requestFocus()
        }
        errorMsg.contains("pass") -> {
            binding.loginPasswordField.error = errorMsg
            binding.loginPasswordField.requestFocus()
        }
        else -> Unit
    }
}