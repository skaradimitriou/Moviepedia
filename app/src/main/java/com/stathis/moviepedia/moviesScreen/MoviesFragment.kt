package com.stathis.moviepedia.moviesScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stathis.moviepedia.databinding.FragmentMoviesBinding
import com.stathis.moviepedia.genresInfoScreen.GenresInfoScreen
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.movieInfoScreen.MovieInfoScreen
import com.stathis.moviepedia.recyclerviews.*


class MoviesFragment : Fragment(), ItemClickListener, GenresClickListener {

    private var moviesViewModel: MoviesViewModel =
        MoviesViewModel()
    private lateinit var binding: FragmentMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initializing the viewModel for this fragment
        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        // Inflate the layout for this fragment
        binding = FragmentMoviesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel lamda expression for upcoming movies
        moviesViewModel.getUpcomingMovies().observe(viewLifecycleOwner,
            Observer<MutableList<Movies>> { t ->
                binding.upcomingMoviesRecView.adapter =
                    UpcomingMoviesAdapter(t, this@MoviesFragment)
            })

        //viewModel lamda expression for trending movies
        moviesViewModel.getTrendingMovies()
            .observe(viewLifecycleOwner, Observer<MutableList<Movies>> { t ->
                binding.popularRecView.adapter =
                    PopularMoviesAdapter(t, this@MoviesFragment)
            })

        //viewModel lamda expression for top rated movies
        moviesViewModel.getTopRatedMovies()
            .observe(viewLifecycleOwner, Observer<MutableList<Movies>> { t ->
                //sorting list by rating and passing it to the adapter
                binding.topRatedRecView.adapter = PopularMoviesAdapter(
                    t.sortedWith(
                        compareBy { it.vote_average }).reversed() as MutableList<Movies>,
                    this@MoviesFragment
                )
                Log.d(
                    "SortedList", t.sortedWith(
                        compareBy { it.vote_average }).reversed().toString()
                )
            })

        //viewModel lambda expression for Movie Genres
        moviesViewModel.getMovieGenres()
            .observe(viewLifecycleOwner, Observer<MutableList<MovieGenres>> { t ->
                binding.genresRecView.adapter = GenresAdapter(t, this@MoviesFragment)
            })

        // Calling my ViewModel functions
        moviesViewModel.getUpcomingMovies()
        moviesViewModel.getMovieGenres()
        moviesViewModel.getTopRatedMovies()
        moviesViewModel.getTrendingMovies()
    }

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
        //
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
}
