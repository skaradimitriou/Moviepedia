package com.stathis.moviepedia.ui.dashboard.search

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractBindingFragment
import com.stathis.moviepedia.databinding.FragmentSearchBinding
import com.stathis.moviepedia.ui.dashboard.search.models.Query
import com.stathis.moviepedia.ui.dashboard.search.models.SearchItem
import com.stathis.moviepedia.listeners.old.SearchItemClickListener


class SearchFragment : AbstractBindingFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private lateinit var query: Query
    private lateinit var viewModel: SearchViewModel

    override fun init() {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        binding.viewModel = viewModel
    }

    override fun startOps() {
        viewModel.getRecentUserQueries()

        binding.searchScreenSearchview.setOnClickListener {
            binding.searchScreenSearchview.isIconified = false
        }

        binding.searchScreenSearchview.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(userQuery: String?): Boolean {
                binding.searchScreenSearchview.clearFocus()
                binding.searchScreenSearchview.setQuery("", false)
                Log.d("Query", userQuery)

                userQuery?.let {
                    query = Query(queryName = userQuery)
                    viewModel.getQueryInfo(query)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.observeData(this,object : SearchItemClickListener{
            override fun onQueryClick(query: Query) {
                viewModel.getQueryInfo(query)
                binding.queryTxt.text = String.format(resources.getString(R.string.search_results_for),query.queryName)
            }
            override fun onSearchItemClick(searchItem: SearchItem) = openItem(searchItem)
        })
    }

    override fun stopOps() {
        viewModel.removeObservers(this)
    }

    private fun openItem(searchItem: SearchItem) {
//        when (searchItem.media_type) {
//            "movie" -> {
//                startActivity(Intent(activity, MovieDetailsActivity::class.java).also {
//                    it.putExtra("MOVIE",searchItem)
//                })
//            }
//            "tv" -> {
//                startActivity(Intent(activity, TvSeriesDetailsActivity::class.java).apply {
//                    putExtra("TV_SERIES", searchItem)
//                })
//            }
//        }
    }
}
