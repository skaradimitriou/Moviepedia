package com.stathis.moviepedia.ui.dashboard

import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractBindingActivity
import com.stathis.moviepedia.databinding.ActivityDashboardBinding

class Dashboard : AbstractBindingActivity<ActivityDashboardBinding>(R.layout.activity_dashboard) {

    override fun init() {
        val navController = findNavController(R.id.fragment)
        binding.bottomNav.setupWithNavController(navController)

        supportActionBar?.hide()
    }

    override fun startOps() {}
    override fun stopOps() {}

    override fun onBackPressed() {
        // You can't go back | Logout if you want to leave
    }
}