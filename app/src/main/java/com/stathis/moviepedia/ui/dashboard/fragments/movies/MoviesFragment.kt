package com.stathis.moviepedia.ui.dashboard.fragments.movies

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
import com.stathis.moviepedia.ui.genresInfoScreen.GenresInfoScreen
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.ui.movieInfoScreen.MovieInfoScreen
import com.stathis.moviepedia.adapters.*


class MoviesFragment : Fragment(), ItemClickListener, GenresClickListener {

    private var moviesViewModel: MoviesViewModel =
        MoviesViewModel()
    private lateinit var binding: FragmentMoviesBinding
    private lateinit var upcomingAdapter: UpcomingAdapter
    private lateinit var trendingAdapter: TrendingAdapter
    private lateinit var topRatedAdapter: TopRatedAdapter
    private lateinit var genresAdapter: GenresAdapter

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

        upcomingAdapter = UpcomingAdapter(this@MoviesFragment)
        binding.upcomingMoviesRecView.adapter = upcomingAdapter

        trendingAdapter = TrendingAdapter(this@MoviesFragment)
        binding.popularRecView.adapter = trendingAdapter

        topRatedAdapter = TopRatedAdapter(this@MoviesFragment)
        binding.topRatedRecView.adapter = topRatedAdapter

        genresAdapter = GenresAdapter( this@MoviesFragment)
        binding.genresRecView.adapter = genresAdapter

        setShimmer()
        observeData()

    }

    private fun observeData(){
        moviesViewModel.getUpcomingMovies().observe(viewLifecycleOwner,
            Observer<MutableList<Movies>> { t ->
                upcomingAdapter.submitList(t as List<Any>?)
                upcomingAdapter.notifyDataSetChanged()
            })

        moviesViewModel.getTrendingMovies()
            .observe(viewLifecycleOwner, Observer<MutableList<Movies>> { t ->
                trendingAdapter.submitList(t as List<Any>?)
                trendingAdapter.notifyDataSetChanged()
            })

        moviesViewModel.getTopRatedMovies()
            .observe(viewLifecycleOwner, Observer<MutableList<Movies>> { t ->
                //sorting list by rating and passing it to the adapter
                topRatedAdapter.submitList(t.sortedWith(
                        compareBy { it.vote_average }).reversed())
                topRatedAdapter.notifyDataSetChanged()
                Log.d(
                    "SortedList", t.sortedWith(
                        compareBy { it.vote_average }).reversed().toString()
                )
            })

        moviesViewModel.getMovieGenres()
            .observe(viewLifecycleOwner, Observer<MutableList<MovieGenres>> { t ->
                genresAdapter.submitList(t as List<Any>?)
                genresAdapter.notifyDataSetChanged()
            })

        moviesViewModel.getUpcomingMovies()
        moviesViewModel.getMovieGenres()
        moviesViewModel.getTopRatedMovies()
        moviesViewModel.getTrendingMovies()
    }

    private fun setShimmer(){
        upcomingAdapter.submitList(moviesViewModel.setShimmer() as List<Any>?)
        trendingAdapter.submitList(moviesViewModel.setShimmer() as List<Any>?)
        topRatedAdapter.submitList(moviesViewModel.setShimmer() as List<Any>?)
        genresAdapter.submitList(moviesViewModel.setShimmer() as List<Any>?)
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
