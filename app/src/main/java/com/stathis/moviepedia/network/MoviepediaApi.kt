package com.stathis.moviepedia.network

import com.stathis.moviepedia.API_KEY
import com.stathis.moviepedia.BASE_URL
import com.stathis.moviepedia.models.genres.MovieGenresFeed
import com.stathis.moviepedia.models.reviews.ReviewsFeed
import com.stathis.moviepedia.models.series.TvSeriesFeed
import com.stathis.moviepedia.models.movies.UpcomingMovies
import com.stathis.moviepedia.models.actor.Actor
import com.stathis.moviepedia.models.actor.KnownMoviesFeed
import com.stathis.moviepedia.models.cast.MovieCastFeed
import com.stathis.moviepedia.ui.dashboard.search.models.SearchItemsFeed
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviepediaApi {

    @GET("movie/upcoming?$API_KEY")
    fun getUpcomindMovies(): Call<UpcomingMovies>

    @GET("trending/movie/day?$API_KEY")
    fun getTrendingMovies(): Call<UpcomingMovies>

    @GET("genre/movie/list?$API_KEY")
    fun getMovieGenres(): Call<MovieGenresFeed>

    @GET("movie/top_rated?$API_KEY")
    fun getTopRatedMovies(): Call<UpcomingMovies>

    @GET("tv/on_the_air?$API_KEY")
    fun getFeaturedTvSeries(): Call<TvSeriesFeed>

    @GET("tv/airing_today?$API_KEY")
    fun getAiringTodayTvSeries(): Call<TvSeriesFeed>

    @GET("tv/top_rated?$API_KEY")
    fun getTopRatedTvSeries(): Call<TvSeriesFeed>

    @GET("tv/top_rated?$API_KEY&language=en-US")
    fun getPopularTvSeries(): Call<TvSeriesFeed>

    @GET("genre/tv/list?$API_KEY")
    fun getTvGenres(): Call<MovieGenresFeed>

    @GET("search/multi?$API_KEY")
    suspend fun getQueryInfo(@Query("query") query: String): Response<SearchItemsFeed>

    @GET("discover/movie?$API_KEY&with_genres=")
    fun getResultsForThisGenre(@Query("genreId") genreId: Int): Call<UpcomingMovies>

    @GET("movie/{movieId}/credits?$API_KEY")
    suspend fun getMovieCastInfo(@Path("movieId") movieId: Int): Response<MovieCastFeed>

    @GET("movie/{movieId}/reviews?$API_KEY")
    suspend fun getMovieReviews(@Path("movieId") movieId: Int): Response<ReviewsFeed>

    @GET("tv/{tvSeriesId}/credits?$API_KEY")
    suspend fun getTvCastInfo(@Path("tvSeriesId") tvSeriesId: Int): Response<MovieCastFeed>

    @GET("tv/{tvSeriesId}/reviews?$API_KEY")
    suspend fun getTvReviews(@Path("tvSeriesId") tvSeriesId: Int): Response<ReviewsFeed>

    @GET("person/{actorId}?$API_KEY")
    fun getActorInfo(@Path("actorId") actorId: Int): Call<Actor>

    @GET("$BASE_URL/person/{actorId}/movie_credits?$API_KEY")
    fun getActorsKnownMovies(@Path("actorId") actorId: Int): Call<KnownMoviesFeed>
}