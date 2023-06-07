package com.manuchar.themoviedb.presentation

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.manuchar.themoviedb.data.api.MoviesApi
import com.manuchar.themoviedb.data.mapper.PopularMoviesMapper
import com.manuchar.themoviedb.domain.model.MovieModel
import java.lang.Exception
import javax.inject.Inject

class MoviePagingSource(
    private val moviesApi: MoviesApi,
    private val mapper: PopularMoviesMapper,
    private val id: Long
) : PagingSource<Int, MovieModel>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {

        return try {
            val position = params.key ?: 1
            val response = moviesApi.getSimilarMovies(id, position)
            LoadResult.Page(
                data = mapper.convert(response).movies,
                prevKey = if (position == 1) null else position - 1,
                nextKey = position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}