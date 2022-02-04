package com.stathis.moviepedia.ui.intro.fragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractBindingFragment
import com.stathis.moviepedia.databinding.FragmentRegisterBinding
import com.stathis.moviepedia.ui.intro.IntroViewModel

class RegisterFragment :
    AbstractBindingFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

    private lateinit var viewModel: IntroViewModel

    override fun init() {
        viewModel = ViewModelProvider(requireActivity()).get(IntroViewModel::class.java)
    }

    override fun startOps() {
        binding.registerAccBtn.setOnClickListener {
            val email = binding.registerEmailField.text.toString()
            val pass = binding.registerPasswordField.text.toString()
            val passConfirm = binding.registerPasswordConfirmField.text.toString()

            viewModel.registerUser(email, pass, passConfirm)
        }

        viewModel.registerSuccess.observe(viewLifecycleOwner, Observer {
            when (it.first) {
                true -> Navigation.findNavController(requireView()).navigate(R.id.registerSuccessFragment)
                false -> handleUiErrors(it.second)
            }
        })
    }

    override fun stopOps() {
        viewModel.registerSuccess.removeObservers(viewLifecycleOwner)
    }

    private fun handleUiErrors(errorMsg: String) = when {
        errorMsg.contains("mail") -> {
            binding.registerEmailField.error = errorMsg
            binding.registerEmailField.requestFocus()
        }
        errorMsg.contains("enter") -> {
            binding.registerPasswordField.error = errorMsg
            binding.registerPasswordField.requestFocus()
        }
        errorMsg.contains("confirm") -> {
            binding.registerPasswordConfirmField.error = errorMsg
            binding.registerPasswordConfirmField.requestFocus()
        }
        else -> Unit
    }
}