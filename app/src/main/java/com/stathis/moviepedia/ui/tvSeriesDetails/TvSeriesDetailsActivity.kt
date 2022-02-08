package com.stathis.moviepedia.ui.tvSeriesDetails

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractBindingActivity
import com.stathis.moviepedia.databinding.ActivityTvSeriesInfoScreenBinding
import com.stathis.moviepedia.listeners.CastCallback
import com.stathis.moviepedia.models.TvSeries
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.ui.castDetails.CastDetailsActivity

class TvSeriesDetailsActivity : AbstractBindingActivity<ActivityTvSeriesInfoScreenBinding>(R.layout.activity_tv_series_info_screen) {

    private lateinit var viewModel: TvSeriesDetailsViewModel
    private lateinit var model : TvSeries
    private lateinit var myFavoriteBtn : MenuItem

    override fun init() {
        viewModel = ViewModelProvider(this).get(TvSeriesDetailsViewModel::class.java)

        binding.viewModel = viewModel
    }

    override fun startOps() {
        val data = intent.getParcelableExtra<TvSeries>("TV_SERIES") ?: null
        data?.let {
            model = it
            binding.model = it

            supportActionBar?.title = model.original_name
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            viewModel.getTvSeriesInfo(model.id)
        }

        viewModel.observe(this, object : CastCallback {
            override fun onCastClick(cast: Cast) {
                startActivity(Intent(this@TvSeriesDetailsActivity, CastDetailsActivity::class.java).putExtra("ACTOR_ID", cast.id))
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
                true -> Snackbar.make(binding.tvSeriesDetailsParent,resources.getString(R.string.network_error),
                    Snackbar.LENGTH_LONG).show()
                false -> Unit
            }
        })

        viewModel.castDataError.observe(this, Observer {
            when(it){
                true -> Snackbar.make(binding.tvSeriesDetailsParent,resources.getString(R.string.network_error),
                    Snackbar.LENGTH_LONG).show()
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
            putExtra(Intent.EXTRA_TEXT, model)
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
                true -> viewModel.addToFavorites(model)
                false -> viewModel.removeFromFavorites(model.id)
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