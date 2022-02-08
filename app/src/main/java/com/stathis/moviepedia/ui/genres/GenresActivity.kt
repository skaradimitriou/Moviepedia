package com.stathis.moviepedia.ui.genres


import android.content.Intent
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractBindingActivity
import com.stathis.moviepedia.databinding.ActivityGenresInfoScreenBinding
import com.stathis.moviepedia.listeners.MovieTypeCallback
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.ui.movieDetails.MovieDetailsActivity

class GenresActivity : AbstractBindingActivity<ActivityGenresInfoScreenBinding>(R.layout.activity_genres_info_screen) {

    private var genreId: Int = 0
    private lateinit var viewModel: GenresViewModel

    override fun init() {
        viewModel = ViewModelProvider(this).get(GenresViewModel::class.java)
        binding.viewModel = viewModel
    }

    override fun startOps() {
        val genreId = intent.getIntExtra("GENRE_ID", 0)
        val genreName = intent.getStringExtra("GENRE_NAME") ?: ""

        supportActionBar?.title = String.format(resources.getString(R.string.genres_screen_movie_title),genreName)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getResultsForThisGenre(genreId)

        binding.genresScreenRefreshLayout.setOnRefreshListener {
            viewModel.getResultsForThisGenre(genreId)
        }

        viewModel.observe(this, object : MovieTypeCallback{
            override fun onMovieTap(movie: Movies) = openMovie(movie)
        })

        viewModel.data.observe(this, Observer {
            when(it.isNullOrEmpty()){
                false -> binding.genresScreenRefreshLayout.isRefreshing = false
                else -> Unit
            }
        })
    }

    override fun stopOps() {
        viewModel.release(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        android.R.id.home -> {
            onBackPressed()
            true
        }

        else -> false
    }

    private fun openMovie(movie : Movies) {
            startActivity(Intent(this, MovieDetailsActivity::class.java).also {
                when(movie.name.isNullOrBlank()){
                    true -> it.putExtra("MOVIE_NAME", movie.title)
                    false -> it.putExtra("MOVIE_NAME", movie.name)
                }

                it.putExtra("MOVIE_ID", movie.id)
                it.putExtra("MOVIE_PHOTO", movie.backdrop_path)
                it.putExtra("MOVIE_PHOTO", movie.poster_path)
                it.putExtra("RELEASE_DATE", movie.release_date)
                it.putExtra("DESCRIPTION", movie.overview)
                it.putExtra("RATING", movie.vote_average.toString())
            })
    }
}