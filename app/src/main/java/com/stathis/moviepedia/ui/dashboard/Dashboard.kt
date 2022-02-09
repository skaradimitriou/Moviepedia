package com.stathis.moviepedia.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.databinding.ActivityDashboardBinding
import com.stathis.moviepedia.ui.dashboard.search.SearchFragment
import com.stathis.moviepedia.ui.userProfile.UserViewModel

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val navView: BottomNavigationView = findViewById(R.id.bottom_nav)
        val navController = findNavController(R.id.fragment)

        navView.setupWithNavController(navController)

        supportActionBar?.hide()

        //viewModel.getUserPhoto()

//        binding.userProfileImg.setOnClickListener {
//            startActivity(Intent(this, UserProfile::class.java))
//        }
//
//        binding.searchView.setOnClickListener {
//            binding.searchView.isIconified = false
//        }
//
//        binding.searchView.setOnQueryTextListener(object :
//            androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                binding.searchView.clearFocus()
//                binding.searchView.setQuery("", false)
//                Log.d("HELLO", query)
//                val bundle = bundleOf("QUERY" to query)
//                searchFragment =
//                    SearchFragment()
//                supportFragmentManager.beginTransaction().replace(R.id.fragment, searchFragment)
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                    .commit()
//                searchFragment.arguments = bundle
//                Log.d("BUNDLE", bundle.toString())
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//        })

    }

    override fun onBackPressed() {
        // You can't go back | Logout if you want to leave
    }
}