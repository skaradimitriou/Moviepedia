package com.stathis.moviepedia

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import com.stathis.moviepedia.databinding.ActivityDashboardBinding
import com.stathis.moviepedia.mainScreen.DashboardFragment
import com.stathis.moviepedia.moviesScreen.MoviesFragment
import com.stathis.moviepedia.searchScreen.SearchFragment
import com.stathis.moviepedia.tvSeriesScreen.TvSeriesFragment
import com.stathis.moviepedia.userProfile.UserProfile
import com.stathis.moviepedia.userProfile.UserViewModel

class Dashboard : AppCompatActivity() {

    private lateinit var storage: FirebaseStorage
    private lateinit var searchFragment: SearchFragment
    private lateinit var dashboardFragment: DashboardFragment
    private lateinit var moviesFragment: MoviesFragment
    private lateinit var tvSeriesFragment: TvSeriesFragment
    private lateinit var binding:ActivityDashboardBinding
    private var userViewModel: UserViewModel =
        UserViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        userViewModel.retrieveUserImg().observe(this, Observer<Bitmap>{ img ->
            Log.d("profile image path", img.toString())
            binding.userProfileImg.setImageBitmap(img)
        })

        userViewModel.retrieveUserImg()

        binding.userProfileImg.setOnClickListener{
            startActivity(Intent(this, UserProfile::class.java))
        }

        binding.searchView.setOnClickListener{
            binding.searchView.isIconified = false
        }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                binding.searchView.setQuery("",false)
                Log.d("HELLO",query)
                val bundle = bundleOf("QUERY" to query)
                searchFragment =
                    SearchFragment()
                supportFragmentManager.beginTransaction().replace(R.id.content, searchFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                searchFragment.arguments = bundle
                Log.d("BUNDLE",bundle.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        dashboardFragment = DashboardFragment()
        supportFragmentManager.beginTransaction().replace(R.id.content, dashboardFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    dashboardFragment =
                        DashboardFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.content, dashboardFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_movies -> {
                    moviesFragment =
                        MoviesFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.content, moviesFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_tv -> {
                   tvSeriesFragment =
                       TvSeriesFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.content, tvSeriesFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_search -> {
                    searchFragment =
                        SearchFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.content, searchFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            bottomNav.selectedItemId = R.id.nav_home
            false
        })
    }
}