package com.stathis.moviepedia.ui.dashboard.fragments.all

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stathis.moviepedia.ui.genresInfoScreen.GenresInfoScreen
import com.stathis.moviepedia.ui.movieInfoScreen.MovieInfoScreen
import com.stathis.moviepedia.databinding.FragmentDashboardBinding
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.adapters.*
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : Fragment(), ItemClickListener, GenresClickListener,
    FavoriteClickListener {

    private lateinit var upcomingAdapter: UpcomingAdapter
    private lateinit var trendingAdapter: TrendingAdapter
    private lateinit var topRatedAdapter: TopRatedAdapter
    private lateinit var genresAdapter: GenresAdapter
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

        upcomingAdapter = UpcomingAdapter(this@DashboardFragment)
        binding.upcomingMoviesRecView.adapter = upcomingAdapter

        trendingAdapter = TrendingAdapter(this@DashboardFragment)
        binding.popularRecView.adapter = trendingAdapter

        genresAdapter = GenresAdapter(this@DashboardFragment)
        binding.genresRecView.adapter = genresAdapter

        topRatedAdapter = TopRatedAdapter(this@DashboardFragment)
        binding.topRatedRecView.adapter = topRatedAdapter

        //adapter items start shimmering until they observe the movies
        setShimmer()
        observeData()

    }

    private fun observeData() {
        moviesViewModel.UpComingMoviesCall().observe(viewLifecycleOwner,
            Observer<MutableList<Movies>> { t ->
                Log.d("T", t.toString())
                upcomingAdapter.submitList(t as List<Any>?)
                upcomingAdapter.notifyDataSetChanged()
            })

        moviesViewModel.TrendingMoviesCall().observe(viewLifecycleOwner,
            Observer<MutableList<Movies>> { t ->
                Log.d("T", t.toString())
                trendingAdapter.submitList(t as List<Any>?)
                trendingAdapter.notifyDataSetChanged()
            })

        moviesViewModel.TopRatedMoviesCall().observe(viewLifecycleOwner,
            Observer<MutableList<Movies>> { t ->
                Log.d("T", t.toString())
                // sorting list by rating and passing it to the adapter
                topRatedAdapter.submitList(
                    t?.sortedWith(
                        compareBy { it.vote_average })?.reversed()
                )
                topRatedAdapter.notifyDataSetChanged()
            })

        moviesViewModel.getFavoriteMovies()
            .observe(viewLifecycleOwner,
                Observer<MutableList<FavoriteMovies>> { t ->
                    if (t?.size == 0) {
                        userFav.visibility = View.GONE
                    } else {
                        val favoriteAdapter = FavoriteAdapter(this@DashboardFragment)
                        favoriteAdapter.submitList(t as List<Any?>)
                        binding.favoriteMoviesRV.adapter = favoriteAdapter
                    }
                })

        moviesViewModel.getMovieGenres()
            .observe(viewLifecycleOwner,
                Observer<MutableList<MovieGenres>> { t ->
                    genresAdapter.submitList(t as List<Any>?)
                    genresAdapter.notifyDataSetChanged()
                })

        moviesViewModel.UpComingMoviesCall()
        moviesViewModel.TrendingMoviesCall()
        moviesViewModel.TopRatedMoviesCall()
        moviesViewModel.getFavoriteMovies()
        moviesViewModel.getMovieGenres()
    }


    private fun setShimmer() {
        upcomingAdapter.submitList(moviesViewModel.setShimmer() as List<Any>?)
        trendingAdapter.submitList(moviesViewModel.setShimmer() as List<Any>?)
        topRatedAdapter.submitList(moviesViewModel.setShimmer() as List<Any>?)
        genresAdapter.submitList(moviesViewModel.setShimmer() as List<Any>?)
    }

    /* handles movie clicks.
    * The point was to send the movie data to the movie info screen*/
    override fun onItemClick(movies: Movies) {
        startActivity(Intent(activity, MovieInfoScreen::class.java).apply {
            if (movies.name.isNullOrBlank()) {
                putExtra("MOVIE_NAME", movies.title)
                Log.d("Movie Name Clicked", movies.title)
            } else {
                putExtra("MOVIE_NAME", movies.name)
                Log.d("Movie Name Clicked", movies.name)
            }
            putExtra("MOVIE_ID", movies.id)
            putExtra("MOVIE_PHOTO", movies.backdrop_path)
            putExtra("MOVIE_PHOTO", movies.poster_path)
            putExtra("RELEASE_DATE", movies.release_date)
            putExtra("DESCRIPTION", movies.overview)
            putExtra("RATING", movies.vote_average.toString())
        })
    }

    override fun onTvSeriesClick(tvSeries: TvSeries) {
        //
    }

    override fun onClick(v: View?) {
        //
    }

    override fun onGenreClick(movieGenres: MovieGenres) {
        startActivity(Intent(activity, GenresInfoScreen::class.java).apply {
            putExtra("GENRE_ID", movieGenres.id)
            putExtra("GENRE_NAME", movieGenres.name)
        })
    }

    override fun onFavoriteMoviesClick(favoriteMovies: FavoriteMovies) {
        startActivity(Intent(activity, MovieInfoScreen::class.java).apply {
            putExtra("MOVIE_ID",favoriteMovies.id)
            putExtra("MOVIE_NAME", favoriteMovies.title)
            putExtra("MOVIE_PHOTO", favoriteMovies.photo)
            putExtra("RELEASE_DATE", favoriteMovies.releaseDate)
            putExtra("DESCRIPTION", favoriteMovies.description)
            putExtra("RATING", favoriteMovies.movie_rating.toString())
        })
    }

    override fun onFavoriteTvSeriesClick(favoriteTvSeries: FavoriteTvSeries) {
        //
    }
}