package com.manuchar.themoviedb.data.respository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.manuchar.themoviedb.data.api.MoviesApi
import com.manuchar.themoviedb.data.mapper.PopularMoviesMapper
import com.manuchar.themoviedb.domain.model.MovieModel
import com.manuchar.themoviedb.domain.repository.MoviesRepository
import com.manuchar.themoviedb.presentation.MoviePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi,
    private val moviesMapper: PopularMoviesMapper,
) : MoviesRepository {


    override fun getPopularMovies(page: Int): Flow<List<MovieModel>> =
        flow {
            emit(moviesApi.getPopularMovies(page))
        }.map {
            moviesMapper.convert(it).movies
        }


    override fun getSimilarMovies(seriesId: Long): Flow<PagingData<MovieModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, enablePlaceholders = false, initialLoadSize = 1
            ), pagingSourceFactory = {
                MoviePagingSource(moviesApi, moviesMapper, seriesId)
            }, initialKey = 1
        ).flow
    }
}