package com.manuchar.themoviedb.domain.interactors

import com.manuchar.themoviedb.domain.repository.MoviesRepository
import com.manuchar.themoviedb.utlis.IoDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetSimilarMoviesUseCase @Inject constructor(
    @IoDispatcher val coroutineContext: CoroutineContext,
    private val moviesApi: MoviesRepository
) {
    fun invoke(movieId: Long) = moviesApi.getSimilarMovies(movieId).flowOn(coroutineContext)
}