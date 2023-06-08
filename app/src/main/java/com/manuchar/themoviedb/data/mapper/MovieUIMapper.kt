package com.manuchar.themoviedb.data.mapper

import com.manuchar.themoviedb.domain.model.MovieModel
import com.manuchar.themoviedb.presentation.popular.model.PopularMoviesItem
import javax.inject.Inject

class MovieUIMapper @Inject constructor() {

    fun convert(movie: MovieModel) = with(movie) {
        PopularMoviesItem.Item(
            id,
            overview,
            imageUrl,
            name,
            averageRating
        )
    }

}