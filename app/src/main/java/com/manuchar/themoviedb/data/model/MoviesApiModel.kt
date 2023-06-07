package com.manuchar.themoviedb.data.model

import com.google.gson.annotations.SerializedName


data class ResultApiModel(@SerializedName("results") val movies: List<MovieApiModel>)

data class MovieApiModel(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("vote_average") val averageRating: Double,
    @SerializedName("backdrop_path") val imageUrl: String?,
    @SerializedName("poster_path") val poster: String?,
    @SerializedName("overview") val overview: String
)
