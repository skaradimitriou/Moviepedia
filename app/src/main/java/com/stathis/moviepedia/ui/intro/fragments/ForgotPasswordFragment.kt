package com.stathis.moviepedia.ui.intro.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractBindingFragment
import com.stathis.moviepedia.databinding.FragmentForgotPasswordBinding
import com.stathis.moviepedia.ui.intro.IntroViewModel
import kotlinx.android.synthetic.main.account_success_view.view.*
import kotlinx.android.synthetic.main.fragment_forgot_password.*

class ForgotPasswordFragment : AbstractBindingFragment<FragmentForgotPasswordBinding>(R.layout.fragment_forgot_password) {

    private lateinit var viewModel : IntroViewModel

    override fun init() {
        viewModel = ViewModelProvider(requireActivity()).get(IntroViewModel::class.java)
    }

    override fun startOps() {
        binding.sendActivationEmail.setOnClickListener {
            viewModel.verifyEmail(forgot_email_field.text.toString())
        }

        viewModel.userVerified.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> showSuccessDialog()
                false -> Toast.makeText(requireContext(), "User not found", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun stopOps() {
        viewModel.userVerified.removeObservers(viewLifecycleOwner)
    }

    private fun showSuccessDialog() {
        val successDialog = LayoutInflater.from(requireContext()).inflate(R.layout.account_success_view, null)
        val successBuilder = AlertDialog.Builder(requireContext())
            .setView(successDialog).show()
        successDialog.redirect_txt.text = "Follow the steps in your e-mail to reset your password"
        Handler().postDelayed({
            successBuilder.dismiss()
            //startActivity(Intent(requireContext(), IntroScreen::class.java))
        }, 4000)
    }
}