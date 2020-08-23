package com.stathis.moviepedia

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.fragments.*
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.recyclerviews.GenresAdapter
import com.stathis.moviepedia.recyclerviews.PopularMoviesAdapter
import com.stathis.moviepedia.recyclerviews.UpcomingMoviesAdapter
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.Call
import okhttp3.MultipartBody.Part.Companion.create
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URI.create

class Dashboard : AppCompatActivity() {

    private lateinit var userProfileImg: CircleImageView
    private lateinit var storage: FirebaseStorage
    private lateinit var searchBar: androidx.appcompat.widget.SearchView
    private lateinit var searchFragment: SearchFragment
    private lateinit var dashboardFragment: DashboardFragment
    private lateinit var moviesFragment: MoviesFragment
    private lateinit var tvSeriesFragment: TvSeriesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        userProfileImg = findViewById(R.id.userProfileImg)
        getUserProfileImg()
        userProfileImg.setOnClickListener{
            startActivity(Intent(this, UserProfile::class.java))
        }

        searchBar = findViewById(R.id.searchView)
        searchBar.setOnClickListener{
            searchBar.isIconified = false
        }

        searchBar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchBar.clearFocus()
                searchBar.setQuery("",false)
                Log.d("HELLO",query)
                val bundle = bundleOf("QUERY" to query)
                searchFragment = SearchFragment()
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
                R.id.nav_search -> {
                    searchFragment = SearchFragment()
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

    private fun getUserProfileImg() {
        storage = FirebaseStorage.getInstance()
        var imageRef: StorageReference =
            storage.reference.child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        imageRef.getBytes(1024 * 1024).addOnSuccessListener { bytes ->
            var userPhoto: CircleImageView = findViewById(R.id.userProfileImg)
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            userPhoto.setImageBitmap(bitmap)

        }.addOnFailureListener {
            // Handle any errors
        }
        imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
            Log.d("DownloadUrl", downloadUrl.toString())
        }.addOnFailureListener { it ->
            Log.d("DownloadUrl", it.toString())
        }
    }
}