package com.manuchar.themoviedb.data.mapper

import com.manuchar.themoviedb.domain.model.MovieModel
import com.manuchar.themoviedb.presentation.popular.model.PopularMoviesItem
import javax.inject.Inject

class MovieUIMapper @Inject constructor() {

    fun convert(movieList: List<MovieModel>) = with(movieList) {
        map {
            PopularMoviesItem.Item(
                it.id,
                it.overview,
                it.imageUrl,
                it.name,
                it.averageRating
            )
        }
    }

}