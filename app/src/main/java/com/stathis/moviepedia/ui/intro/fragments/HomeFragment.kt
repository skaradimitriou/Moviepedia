package com.stathis.moviepedia.ui.intro.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractBindingFragment
import com.stathis.moviepedia.databinding.FragmentHomeBinding
import com.stathis.moviepedia.ui.dashboard.Dashboard
import com.stathis.moviepedia.ui.forgotPassword.forgotPass.ForgotPassword
import com.stathis.moviepedia.ui.intro.IntroViewModel


class HomeFragment : AbstractBindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private lateinit var viewModel : IntroViewModel

    override fun init() {
        viewModel = ViewModelProvider(requireActivity()).get(IntroViewModel::class.java)

        when(viewModel.auth.currentUser != null){
            true -> startActivity(Intent(requireContext(), Dashboard::class.java))
        }
    }

    override fun startOps() {
        binding.loginBtn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.loginFragment)
        }

        binding.registerBtn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.registerFragment)
        }

        binding.forgotPassBtn.setOnClickListener {
            startActivity(Intent(requireContext(),ForgotPassword::class.java))
        }
    }

    override fun stopOps() {}
}