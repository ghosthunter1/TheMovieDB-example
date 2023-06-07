package com.manuchar.themoviedb.data.api

import com.manuchar.themoviedb.data.model.ResultApiModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): ResultApiModel

    @GET("{series_id}/similar")
    suspend fun getSimilarMovies(
        @Path("series_id") seriesId: Long, @Query("page") page: Int
    ): ResultApiModel

}