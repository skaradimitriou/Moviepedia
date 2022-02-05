package com.stathis.moviepedia.ui.tvSeriesInfoScreen

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractActivity
import com.stathis.moviepedia.databinding.ActivityTvSeriesInfoScreenBinding
import com.stathis.moviepedia.listeners.old.LocalClickListener
import com.stathis.moviepedia.models.FavoriteTvSeries
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.ui.castDetails.CastDetailsActivity

class TvSeriesInfoScreen : AbstractActivity() {

    private var tvSeriesId: Int = 0
    private lateinit var tvSeriesPhoto: String
    private lateinit var tvSeriesTitle: String
    private lateinit var tvSeriesRating: String
    private lateinit var tvSeriesReleaseDate: String
    private lateinit var tvSeriesDescription: String
    private lateinit var binding: ActivityTvSeriesInfoScreenBinding
    private lateinit var viewModel: TvSeriesInfoScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvSeriesInfoScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initLayout() {
        viewModel = TvSeriesInfoScreenViewModel()
    }

    override fun running() {
        getIntentInfo()

        viewModel.getUserFavorites(tvSeriesId)

        binding.castRecView.adapter = viewModel.castAdapter
        binding.reviewsRecView.adapter = viewModel.reviewsAdapter

        viewModel.getCastInfo(tvSeriesId)
        viewModel.getTvSeriesReviews(tvSeriesId)

        observeViewModel()

        binding.likeBtn.setOnClickListener {
            val whiteFavBtnColor: Drawable.ConstantState? =
                resources.getDrawable(R.drawable.ic_favorite_icon, this.theme).constantState
            val likeBtnColor = binding.likeBtn.drawable.constantState

            if (likeBtnColor!!.equals(whiteFavBtnColor)) {
                val fav = FavoriteTvSeries(
                    tvSeriesId,
                    tvSeriesPhoto,
                    tvSeriesTitle,
                    tvSeriesRating.toDouble(),
                    tvSeriesReleaseDate,
                    tvSeriesDescription
                )
                viewModel.addToFavorites(fav)
            } else {
                viewModel.removeFromFavorites(tvSeriesId)
            }
        }

        binding.shareBtn.setOnClickListener {
            share()
        }
    }

    private fun observeViewModel() {
        viewModel.observeDataFromApi(this, object : LocalClickListener {
            override fun onCastClick(cast: Cast) {
                startActivity(Intent(applicationContext, CastDetailsActivity::class.java).putExtra("ACTOR_ID", cast.id))
            }
        })

        viewModel.isFavorite.observe(this, Observer {
            when (it) {
                true -> {
                    binding.likeBtn.setImageResource(R.drawable.ic_favorite_white)
                }

                false -> {
                    binding.likeBtn.setImageResource(R.drawable.ic_favorite_icon)
                }
            }
        })
    }

    override fun stopped() {
        viewModel.removeObservers(this)
        viewModel.isFavorite.removeObservers(this)
    }

    private fun getIntentInfo() {
        tvSeriesId = intent.getIntExtra("TV_SERIES_ID", tvSeriesId)
        tvSeriesPhoto = intent.getStringExtra("TV_SERIES_PHOTO")
        tvSeriesTitle = intent.getStringExtra("TV_SERIES_NAME")
        tvSeriesRating = intent.getStringExtra("TV_SERIES_RATING")
        tvSeriesReleaseDate = intent.getStringExtra("TV_SERIES_RELEASE_DATE")
        tvSeriesDescription = intent.getStringExtra("TV_SERIES_DESCRIPTION")

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$tvSeriesPhoto")
            .into(binding.posterImg)

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$tvSeriesPhoto")
            .into(binding.imgView)

        binding.tvSeriesTxt.text = tvSeriesTitle

        val rating = tvSeriesRating.toDouble()
        binding.ratingBar.rating = rating.toFloat() / 2

        binding.description.text = tvSeriesDescription
        binding.releaseDate.text = tvSeriesReleaseDate
    }

    private fun share() {
        startActivity(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, tvSeriesTitle)
        })
    }
}
