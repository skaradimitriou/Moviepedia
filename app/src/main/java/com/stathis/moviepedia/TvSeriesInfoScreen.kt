package com.stathis.moviepedia

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.databinding.ActivityTvSeriesInfoScreenBinding
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.FavoriteTvSeries
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.models.cast.MovieCastFeed
import com.stathis.moviepedia.recyclerviews.CastAdapter
import kotlinx.android.synthetic.main.activity_movie_info_screen.*
import kotlinx.android.synthetic.main.activity_movie_info_screen.shareBtn
import kotlinx.android.synthetic.main.activity_tv_series_info_screen.*
import kotlinx.android.synthetic.main.popular_item_row.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class TvSeriesInfoScreen : AppCompatActivity() {

    private var tvSeriesId: Int = 0
    private lateinit var tvSeriesPhoto: String
    private lateinit var tvSeriesTitle: String
    private lateinit var tvSeriesRating: String
    private lateinit var tvSeriesReleaseDate: String
    private lateinit var tvSeriesDescription: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var url: String
    private lateinit var request: Request
    private var client: OkHttpClient = OkHttpClient()
    private var castInfo: MutableList<Cast> = mutableListOf()
    private lateinit var binding:ActivityTvSeriesInfoScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvSeriesInfoScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUserFavorites()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        getIntentInfo()
        getCastInfo()

        binding.likeBtn.setOnClickListener {
            val whiteFavBtnColor: Drawable.ConstantState? =
                resources.getDrawable(R.drawable.ic_favorite_icon, this.theme).constantState
            val likeBtnColor = binding.likeBtn.drawable.constantState

            if (likeBtnColor!!.equals(whiteFavBtnColor)) {
                addToFavorites()
            } else {
                removeFromFavorites()
            }
        }

        binding.shareBtn.setOnClickListener {
            share()
        }
    }

    private fun getIntentInfo() {
        tvSeriesId = intent.getIntExtra("TV_SERIES_ID", tvSeriesId)
        tvSeriesPhoto = intent.getStringExtra("TV_SERIES_PHOTO")
        tvSeriesTitle = intent.getStringExtra("TV_SERIES_NAME")
        tvSeriesRating = intent.getStringExtra("TV_SERIES_RATING")
        tvSeriesReleaseDate = intent.getStringExtra("TV_SERIES_RELEASE_DATE")
        tvSeriesDescription = intent.getStringExtra("TV_SERIES_DESCRIPTION")

        setTvSeriesPhoto()
        setTvSeriesTitle()
        setTvSeriesRating()
        setTvSeriesDescription()
        setTvSeriesReleaseDate()
    }

    private fun setTvSeriesPhoto() {
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$tvSeriesPhoto")
            .into(binding.posterImg)

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$tvSeriesPhoto")
            .into(binding.imgView)
    }

    private fun setTvSeriesTitle() {
        binding.tvSeriesTxt.text = tvSeriesTitle
    }

    private fun setTvSeriesRating() {
        //converting rating toDouble()
        var rating = tvSeriesRating.toDouble()
        binding.rating.text = "$rating/10"
        //applying rating to the ratingBar
        binding.ratingBar.rating = rating.toFloat() / 2
    }

    private fun setTvSeriesDescription() {
        binding.description.text = tvSeriesDescription
    }

    private fun setTvSeriesReleaseDate() {
        binding.releaseDate.text = tvSeriesReleaseDate
    }

    private fun addToFavorites() {
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteTvSeries")
            .child(tvSeriesId.toString()).setValue(
                FavoriteTvSeries(
                    tvSeriesId,
                    tvSeriesPhoto,
                    tvSeriesTitle,
                    tvSeriesRating.toDouble(),
                    tvSeriesReleaseDate,
                    tvSeriesDescription
                )
            )
        binding.likeBtn.setImageResource(R.drawable.ic_favorite_white)
    }

    private fun getUserFavorites() {
        //adds a new favorite to the favorite movie list
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteTvSeries")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (i in p0.children) {
                            val fav = i.getValue(FavoriteTvSeries::class.java)
                            if (fav?.id == tvSeriesId) {
                                binding.likeBtn.setImageResource(R.drawable.ic_favorite_white)
                            }
                            Log.d("i", i.toString())
                        }
                    }
                }
            })
    }

    private fun removeFromFavorites() {
        //adds a new favorite to the favorite movie list
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteTvSeries")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (i in p0.children) {
                            val favorite = i.getValue(FavoriteTvSeries::class.java)
                            if (favorite?.id == tvSeriesId) {
                                i.ref.removeValue()
                                binding.likeBtn.setImageResource(R.drawable.ic_favorite_icon)
                            }
                        }
                    }
                }
            })
    }

    private fun getCastInfo() {
        url =
            "https://api.themoviedb.org/3/tv/$tvSeriesId/credits?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val cast = gson.fromJson(body, MovieCastFeed::class.java)
                Log.d("Response", cast.toString())

                if (cast != null) {
                    castInfo = ArrayList(cast.cast)
                    Log.d("Cast", castInfo.toString())

                    runOnUiThread {
                        binding.castRecView.adapter = CastAdapter(castInfo)
                    }
                }
            }
        })
    }

    private fun share() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        startActivity(shareIntent)
    }
}
