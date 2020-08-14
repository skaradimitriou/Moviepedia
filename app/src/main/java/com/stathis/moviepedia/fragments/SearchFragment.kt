package com.stathis.moviepedia.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.GsonBuilder

import com.stathis.moviepedia.R
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class SearchFragment : Fragment() {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient
    private var apiKey: String = "b36812048cc4b54d559f16a2ff196bc5"
    private lateinit var bundle: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getQueryInfo()
    }

    private fun getQueryInfo() {
        //getting the QUERY from the search bar
        bundle = this.arguments?.getString("QUERY").toString()
        //building the link & calling the API for results
        url = "https://api.themoviedb.org/3/search/multi?api_key=$apiKey&query=$bundle"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                //
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                Log.d("RESPONSE", body.toString())

            }

        })
    }

}
