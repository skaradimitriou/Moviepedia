package com.stathis.moviepedia.ui.dashboard

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import com.stathis.moviepedia.R
import com.stathis.moviepedia.databinding.ActivityDashboardBinding
import com.stathis.moviepedia.ui.dashboard.fragments.all.DashboardFragment
import com.stathis.moviepedia.ui.dashboard.fragments.movies.MoviesFragment
import com.stathis.moviepedia.ui.searchScreen.SearchFragment
import com.stathis.moviepedia.ui.dashboard.fragments.tvSeries.TvSeriesFragment
import com.stathis.moviepedia.ui.userProfile.UserProfile
import com.stathis.moviepedia.ui.userProfile.UserViewModel

class Dashboard : AppCompatActivity() {

    private lateinit var storage: FirebaseStorage
    private lateinit var searchFragment: SearchFragment
    private lateinit var dashboardFragment: DashboardFragment
    private lateinit var moviesFragment: MoviesFragment
    private lateinit var tvSeriesFragment: TvSeriesFragment
    private lateinit var binding: ActivityDashboardBinding
    private var userViewModel: UserViewModel =
        UserViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        userViewModel.retrieveUserImg().observe(this, Observer<Bitmap> { img ->
            Log.d("profile image path", img.toString())
            binding.userProfileImg.setImageBitmap(img)
        })

        binding.userProfileImg.setOnClickListener {
            startActivity(Intent(this, UserProfile::class.java))
        }

        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                binding.searchView.setQuery("", false)
                Log.d("HELLO", query)
                val bundle = bundleOf("QUERY" to query)
                searchFragment =
                    SearchFragment()
                supportFragmentManager.beginTransaction().replace(R.id.content, searchFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                searchFragment.arguments = bundle
                Log.d("BUNDLE", bundle.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        if (isNetworkAvailable(this)) {
            Log.d("Internet OK", "Internet OK")
            dashboardFragment = DashboardFragment()
            supportFragmentManager.beginTransaction().replace(R.id.content, dashboardFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        } else {
            Log.d("No Internet", "No Internet")
        }

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    dashboardFragment =
                        DashboardFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content, dashboardFragment)
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
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content, tvSeriesFragment)
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

    override fun onBackPressed() {
        // You can't go back | Logout if you want to leave
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}