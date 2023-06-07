package com.manuchar.themoviedb.data.respository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.manuchar.themoviedb.utlis.ApiResult
import com.manuchar.themoviedb.data.api.MoviesApi
import com.manuchar.themoviedb.data.mapper.PopularMoviesMapper
import com.manuchar.themoviedb.domain.model.MovieModel
import com.manuchar.themoviedb.domain.repository.MoviesRepository
import com.manuchar.themoviedb.domain.model.ResultModel
import com.manuchar.themoviedb.presentation.MoviePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi,
    private val moviesMapper: PopularMoviesMapper,
) : MoviesRepository {


    override fun getPopularMovies(page: Int): Flow<ApiResult<ResultModel>> {

        return flow<ApiResult<ResultModel>> {
            emit(
                ApiResult.ApiSuccess(
                    moviesMapper.convert(
                        moviesApi.getPopularMovies(
                            page
                        )
                    )
                )
            )
        }.onStart { emit(ApiResult.ApiLoading()) }
            .catch { emit(ApiResult.ApiException(it)) }


    }

    override fun getSimilarMovies(seriesId: Long): Flow<PagingData<MovieModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 1
            ),
            pagingSourceFactory = {
                MoviePagingSource(moviesApi, moviesMapper, seriesId)
            }, initialKey = 1
        ).flow
    }
}