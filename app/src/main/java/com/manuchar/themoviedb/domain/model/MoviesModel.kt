package com.manuchar.themoviedb.domain.model


data class ResultModel(val movies: List<MovieModel>)

data class MovieModel(
    val id: Long,
    val name: String,
    val averageRating: Double,
    val imageUrl: String?,
    val poster: String?,
    val overview: String
)
