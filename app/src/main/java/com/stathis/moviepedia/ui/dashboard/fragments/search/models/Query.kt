package com.stathis.moviepedia.ui.dashboard.fragments.search.models

import com.stathis.moviepedia.models.LocalModel

class Query(
    var queryName: String
) : LocalModel {
    constructor() : this("")

    override fun equals(model: LocalModel): Boolean = false
}