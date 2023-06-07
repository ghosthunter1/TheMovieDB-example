package com.manuchar.themoviedb.domain.interactors

import com.manuchar.themoviedb.domain.repository.MoviesRepository
import com.manuchar.themoviedb.utlis.IoDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetPopularMoviesUseCase @Inject constructor(
    @IoDispatcher val coroutineContext: CoroutineContext,
    private val repository: MoviesRepository
) {

    fun invoke(page: Int) = repository.getPopularMovies(page).flowOn(coroutineContext)
}