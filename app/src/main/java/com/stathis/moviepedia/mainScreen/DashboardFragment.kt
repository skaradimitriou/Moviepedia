package com.stathis.moviepedia.mainScreen

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stathis.moviepedia.genresInfoScreen.GenresInfoScreen
import com.stathis.moviepedia.movieInfoScreen.MovieInfoScreen
import com.stathis.moviepedia.databinding.FragmentDashboardBinding
import com.stathis.moviepedia.databinding.NoConnectionScreenBinding
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.recyclerviews.*
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : Fragment(), ItemClickListener, GenresClickListener,
    FavoriteClickListener {

    private lateinit var listAdapter: ListAdapter
    private var moviesViewModel: MovAndTvSeriesViewModel =
        MovAndTvSeriesViewModel()
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        moviesViewModel = ViewModelProvider(this).get(MovAndTvSeriesViewModel::class.java)
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = ListAdapter(this@DashboardFragment)

        moviesViewModel.UpComingMoviesCall().observe(this, object : Observer<MutableList<Movies>> {
            override fun onChanged(t: MutableList<Movies>?) {
                Log.d("T", t.toString())
                binding.upcomingMoviesRecView.adapter =
                    UpcomingMoviesAdapter(t, this@DashboardFragment)
            }
        })

        moviesViewModel.TrendingMoviesCall().observe(this, object : Observer<MutableList<Movies>> {
            override fun onChanged(t: MutableList<Movies>?) {
                Log.d("T", t.toString())
                listAdapter.submitList(t as List<Any>?)
                binding.popularRecView.adapter = listAdapter
            }
        })

        moviesViewModel.TopRatedMoviesCall().observe(this, object : Observer<MutableList<Movies>> {
            override fun onChanged(t: MutableList<Movies>?) {
                Log.d("T", t.toString())
                val topRatedAdapter: TopRatedAdapter = TopRatedAdapter(this@DashboardFragment)
                topRatedAdapter.submitList(t?.sortedWith(
                    compareBy { it.vote_average })?.reversed()
                )
//                sorting list by rating and passing it to the adapter
                binding.topRatedRecView.adapter = topRatedAdapter

                Log.d(
                    "SortedList", t?.sortedWith(
                        compareBy { it.vote_average })?.reversed().toString()
                )
            }
        })

        moviesViewModel.getFavoriteMovies()
            .observe(this, object : Observer<MutableList<FavoriteMovies>> {
                override fun onChanged(t: MutableList<FavoriteMovies>?) {
                    if (t?.size == 0) {
                        userFav.visibility = View.GONE
                    } else {
                        val favoriteAdapter = FavoriteAdapter(this@DashboardFragment)
                        favoriteAdapter.submitList(t as List<Any?>)
                        binding.favoriteMoviesRV.adapter = favoriteAdapter
                    }
                }
            })

        moviesViewModel.getMovieGenres()
            .observe(viewLifecycleOwner, object : Observer<MutableList<MovieGenres>> {
                override fun onChanged(t: MutableList<MovieGenres>?) {
                    binding.genresRecView.adapter = GenresAdapter(t, this@DashboardFragment)
                }

            })

        moviesViewModel.UpComingMoviesCall()
        moviesViewModel.TrendingMoviesCall()
        moviesViewModel.TopRatedMoviesCall()
        moviesViewModel.getFavoriteMovies()
        moviesViewModel.getMovieGenres()
    }

    /* handles movie clicks.
    * The point was to send the movie data to the movie info screen*/
    override fun onItemClick(movies: Movies) {
        val movieIntent = Intent(activity, MovieInfoScreen::class.java)
        val name = movies.name
        val title = movies.title
        //converting rating toString() so I can pass it. Double was throwing error
        val rating = movies.vote_average.toString()
        Log.d("rating", rating)
        if (name.isNullOrBlank()) {
            movieIntent.putExtra("MOVIE_NAME", title)
            Log.d("Movie Name Clicked", title)
        } else {
            movieIntent.putExtra("MOVIE_NAME", name)
            Log.d("Movie Name Clicked", name)
        }
        movieIntent.putExtra("MOVIE_ID", movies.id)
        movieIntent.putExtra("MOVIE_PHOTO", movies.backdrop_path)
        movieIntent.putExtra("MOVIE_PHOTO", movies.poster_path)
        movieIntent.putExtra("RELEASE_DATE", movies.release_date)
        movieIntent.putExtra("DESCRIPTION", movies.overview)
        movieIntent.putExtra("RATING", rating)
        startActivity(movieIntent)

    }

    override fun onTvSeriesClick(tvSeries: TvSeries) {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        //
    }

    override fun onGenreClick(movieGenres: MovieGenres) {
        val genresIntent = Intent(activity, GenresInfoScreen::class.java)
        genresIntent.putExtra("GENRE_ID", movieGenres.id)
        genresIntent.putExtra("GENRE_NAME", movieGenres.name)
        startActivity(genresIntent)
    }

    override fun onFavoriteMoviesClick(favoriteMovies: FavoriteMovies) {
        val movieIntent = Intent(activity, MovieInfoScreen::class.java)
        //converting rating toString() so I can pass it. Double was throwing an error
        val rating = favoriteMovies.movie_rating.toString()
        movieIntent.putExtra("MOVIE_NAME", favoriteMovies.title)
        movieIntent.putExtra("MOVIE_PHOTO", favoriteMovies.photo)
        movieIntent.putExtra("RELEASE_DATE", favoriteMovies.releaseDate)
        movieIntent.putExtra("DESCRIPTION", favoriteMovies.description)
        movieIntent.putExtra("RATING", rating)
        startActivity(movieIntent)
    }

    override fun onFavoriteTvSeriesClick(favoriteTvSeries: FavoriteTvSeries) {
        //
    }
}