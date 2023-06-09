package com.manuchar.themoviedb.domain.repository

import androidx.paging.PagingData
import com.manuchar.themoviedb.domain.model.MovieModel
import com.manuchar.themoviedb.utlis.ApiResult
import com.manuchar.themoviedb.domain.model.ResultModel
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getPopularMovies(page: Int): Flow<List<MovieModel>>
    fun getSimilarMovies(seriesId: Long): Flow<PagingData<MovieModel>>
}