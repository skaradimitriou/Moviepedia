package com.stathis.moviepedia.ui.intro.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractBindingFragment
import com.stathis.moviepedia.databinding.FragmentRegisterBinding
import com.stathis.moviepedia.ui.dashboard.Dashboard
import com.stathis.moviepedia.ui.intro.IntroActivity

class RegisterSuccessFragment : AbstractBindingFragment<FragmentRegisterBinding>(R.layout.fragment_register_success) {

    override fun init() {}

    override fun startOps() {
        Handler().postDelayed({
            startActivity(Intent(requireContext(), Dashboard::class.java))
        }, 1500)
    }

    override fun stopOps() {}

}