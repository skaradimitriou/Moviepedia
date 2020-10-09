package com.stathis.moviepedia.ui.tvSeriesInfoScreen

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.stathis.moviepedia.R
import com.stathis.moviepedia.databinding.ActivityTvSeriesInfoScreenBinding
import com.stathis.moviepedia.models.FavoriteTvSeries
import com.stathis.moviepedia.models.Reviews
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.adapters.CastAdapter
import com.stathis.moviepedia.adapters.ReviewsAdapter
import okhttp3.internal.notify

class TvSeriesInfoScreen : AppCompatActivity() {

    private var tvSeriesId: Int = 0
    private lateinit var tvSeriesPhoto: String
    private lateinit var tvSeriesTitle: String
    private lateinit var tvSeriesRating: String
    private lateinit var tvSeriesReleaseDate: String
    private lateinit var castAdapter : CastAdapter
    private lateinit var tvSeriesDescription: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: ActivityTvSeriesInfoScreenBinding
    private lateinit var tvSeriesInfoScreenViewModel: TvSeriesInfoScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvSeriesInfoScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUserFavorites()
        tvSeriesInfoScreenViewModel = TvSeriesInfoScreenViewModel()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        castAdapter = CastAdapter()
        startShimmer()

        getIntentInfo()

        tvSeriesInfoScreenViewModel.getCastInfo(tvSeriesId)
            .observe(this, Observer<MutableList<Cast>> {cast ->
                Log.d("cast is:",cast.toString())
                binding.castRecView.adapter = castAdapter
                castAdapter.submitList(cast as List<Any>?)
                castAdapter.notifyDataSetChanged()
            })

        tvSeriesInfoScreenViewModel.getTvSeriesReviews(tvSeriesId).observe(this, Observer<MutableList<Reviews>>{reviews ->
            Log.d("cast is:",reviews.toString())
            val reviewsAdapter = ReviewsAdapter()
            reviewsAdapter.submitList(reviews as List<Any>?)
            binding.reviewsRecView.adapter = reviewsAdapter
        })

        tvSeriesInfoScreenViewModel.getCastInfo(tvSeriesId)
        tvSeriesInfoScreenViewModel.getTvSeriesReviews(tvSeriesId)

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

    private fun startShimmer(){
        castAdapter.submitList(tvSeriesInfoScreenViewModel.setShimmer() as List<Any>?)
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

    private fun share() {
        startActivity(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, tvSeriesTitle)
        })
    }
}
