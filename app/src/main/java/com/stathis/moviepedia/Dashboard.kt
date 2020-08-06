package com.stathis.moviepedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.fragments.DashboardFragment
import com.stathis.moviepedia.fragments.MoviesFragment
import com.stathis.moviepedia.fragments.TvSeriesFragment
import com.stathis.moviepedia.fragments.UserProfileFragment
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.recyclerviews.GenresAdapter
import com.stathis.moviepedia.recyclerviews.PopularMoviesAdapter
import com.stathis.moviepedia.recyclerviews.UpcomingMoviesAdapter
import okhttp3.Call
import okhttp3.MultipartBody.Part.Companion.create
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URI.create

class Dashboard : AppCompatActivity() {

    private lateinit var dashboardFragment: DashboardFragment
    private lateinit var moviesFragment: MoviesFragment
    private lateinit var tvSeriesFragment: TvSeriesFragment
    private lateinit var userProfileFragment: UserProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        dashboardFragment = DashboardFragment()
        supportFragmentManager.beginTransaction().replace(R.id.content, dashboardFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    dashboardFragment = DashboardFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.content, dashboardFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_movies -> {
                    moviesFragment = MoviesFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.content, moviesFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_tv -> {
                   tvSeriesFragment = TvSeriesFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.content, tvSeriesFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    userProfileFragment = UserProfileFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.content, userProfileFragment)
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