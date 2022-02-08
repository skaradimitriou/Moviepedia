package com.stathis.moviepedia.ui.movieDetails

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractBindingActivity
import com.stathis.moviepedia.databinding.ActivityMovieInfoScreenBinding
import com.stathis.moviepedia.listeners.CastCallback
import com.stathis.moviepedia.models.movies.Movies
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.ui.castDetails.CastDetailsActivity

class MovieDetailsActivity : AbstractBindingActivity<ActivityMovieInfoScreenBinding>(R.layout.activity_movie_info_screen){

    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var movie: Movies
    private lateinit var myFavoriteBtn : MenuItem

    override fun init() {
        viewModel = ViewModelProvider(this).get(MovieDetailsViewModel::class.java)
        binding.viewModel = viewModel
    }

    override fun startOps() {
        val data = intent.getParcelableExtra<Movies>("MOVIE") ?: null
        data?.let { movie = it }

        supportActionBar?.title = movie.title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.model = movie

        viewModel.getAdditionalDetails(movie.id, movie.title)

        viewModel.observe(this, object : CastCallback {
            override fun onCastClick(cast: Cast) {
                startActivity(Intent(this@MovieDetailsActivity, CastDetailsActivity::class.java).apply {
                    putExtra("ACTOR_ID",cast.id)
                })
            }
        })

        viewModel.isFavorite.observe(this, Observer {
            when (it) {
                true -> myFavoriteBtn.setIcon(R.drawable.ic_yellow_star)
                false -> myFavoriteBtn.setIcon(R.drawable.ic_star_outline)
            }
        })

        viewModel.reviews.observe(this, Observer {
            when(it.isEmpty()){
                true -> binding.reviewsHeader.visibility = View.GONE
                false -> Unit
            }
        })

        viewModel.reviewsError.observe(this, Observer {
            when(it){
               true -> Snackbar.make(binding.movieDetailsParent,resources.getString(R.string.network_error),Snackbar.LENGTH_LONG).show()
                false -> Unit
            }
        })

        viewModel.castDataError.observe(this, Observer {
            when(it){
                true -> Snackbar.make(binding.movieDetailsParent,resources.getString(R.string.network_error),Snackbar.LENGTH_LONG).show()
                false -> Unit
            }
        })
    }

    override fun stopOps() {
        viewModel.removeObservers(this)
        viewModel.isFavorite.removeObservers(this)
    }

    private fun share() {
        startActivity(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, movie.title)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.movie_details_menu, menu)

        val favBtn: MenuItem? = menu?.findItem(R.id.favorite_btn)
        val shareBtn: MenuItem? = menu?.findItem(R.id.share_movie)

        favBtn?.let { myFavoriteBtn = it }

        favBtn?.setOnMenuItemClickListener {
            val whiteFabBtn = resources.getDrawable(R.drawable.ic_star_outline, this.theme).constantState

            when(favBtn.icon.constantState == whiteFabBtn){
                true -> viewModel.addToFavorites(movie)
                false -> viewModel.removeFromFavorites(movie.title)
            }
            true
        }

        shareBtn?.setOnMenuItemClickListener {
            share()
            true
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> false
    }
}