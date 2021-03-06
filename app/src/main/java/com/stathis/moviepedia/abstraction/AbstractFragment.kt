package com.stathis.moviepedia.abstraction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class AbstractFragment : Fragment() {

    abstract fun created() : View?

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return created()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout(view)
    }

    abstract fun initLayout(view: View)

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        running()
    }

    abstract fun running()

    override fun onStop() {
        stop()
        super.onStop()
    }

    abstract fun stop()

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}