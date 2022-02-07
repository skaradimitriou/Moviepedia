package com.stathis.moviepedia.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.BR
import com.stathis.moviepedia.models.cast.Cast

class CastViewHolder(val binding : ViewDataBinding,val callback: GenericCallback) : RecyclerView.ViewHolder(binding.root) {

    fun present(data: LocalModel) {
        when (data) {
            is Cast -> {
                binding.setVariable(BR.model,data)
                binding.setVariable(BR.callback,callback)


//                if (localModel.profile_path.isNullOrBlank()) {
//                    itemView.castImg.setImageResource(R.drawable.profile_img_placeholder)
//                } else {
//                    Glide.with(itemView.context)
//                        .load("https://image.tmdb.org/t/p/w500" + localModel.profile_path)
//                        .placeholder(R.drawable.default_img)
//                        .into(itemView.castImg)
//                }
//
//                itemView.castName.text = localModel.name
//
//                itemView.setOnClickListener { callback.onCastClick(localModel) }
            }
        }
    }
}