package com.manuchar.themoviedb.data.mapper

import com.manuchar.themoviedb.BuildConfig
import com.manuchar.themoviedb.data.model.MovieApiModel
import com.manuchar.themoviedb.data.model.ResultApiModel
import com.manuchar.themoviedb.domain.model.MovieModel
import com.manuchar.themoviedb.domain.model.ResultModel
import javax.inject.Inject

class PopularMoviesMapper @Inject constructor() {

    fun convert(popularMoviesResult: ResultApiModel) =
        ResultModel(movies = popularMoviesResult.movies.map { convert(it) })

    private fun convert(movieApiModel: MovieApiModel) =
        with(movieApiModel) {
            MovieModel(
                id,
                name,
                averageRating,
                BuildConfig.IMAGE_BASE_URL + movieApiModel.imageUrl,
                BuildConfig.IMAGE_BASE_URL + movieApiModel.poster,
                overview
            )
        }
}