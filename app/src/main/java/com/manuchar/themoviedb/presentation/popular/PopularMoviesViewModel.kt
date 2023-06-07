package com.manuchar.themoviedb.presentation.popular

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.manuchar.themoviedb.utlis.ApiResult
import com.manuchar.themoviedb.domain.interactors.GetPopularMoviesUseCase
import com.manuchar.themoviedb.domain.model.MovieModel
import com.manuchar.themoviedb.domain.model.ResultModel
import com.manuchar.themoviedb.presentation.base.BaseViewModel
import com.manuchar.themoviedb.presentation.popular.model.PopularMoviesItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

interface PopularMoviesViewModel {

    interface Input {
        fun fetchMore()
        fun retry()
    }

    interface Output {
        val popularMovies: StateFlow<List<PopularMoviesItem>>

    }

    @HiltViewModel
    class ViewModel @Inject constructor(
        private val getPopularMoviesUseCase: GetPopularMoviesUseCase
    ) : BaseViewModel(), Input, Output {

        val input: Input = this
        val out: Output = this

        private val moviesFlow = MutableSharedFlow<Unit>(replay = 1)
        private var currentPage = 1
        private var isLoading = false

        private val _popularMovies: MutableStateFlow<List<PopularMoviesItem>> =
            MutableStateFlow(emptyList())

        override val popularMovies: StateFlow<List<PopularMoviesItem>> =
            _popularMovies.asStateFlow()


        init {

            viewModelScope.launch {

                merge(onInit(), moviesFlow).flatMapLatest {
                    getPopularMoviesUseCase.invoke(currentPage)
                }.collectLatest { result ->

                    result.onSuccess {
                        isLoading = false
                        currentPage += 1
                    }

                    result.onLoading {
                        isLoading = true
                    }

                    _popularMovies.getAndUpdate {
                        it.filterIsInstance<PopularMoviesItem.Item>().plus(buildList(result))
                    }
                }

            }

        }

        override fun fetchMore() {
            if (!isLoading) moviesFlow.tryEmit(Unit)
        }

        override fun retry() {
            moviesFlow.tryEmit(Unit)
        }


        private fun buildList(moviesResponse: ApiResult<ResultModel>): List<PopularMoviesItem> {

            return when (moviesResponse) {
                is ApiResult.ApiSuccess -> {
                    moviesResponse.data.movies.map { item ->
                        PopularMoviesItem.Item(
                            item.id, item.overview, item.imageUrl, item.name, item.averageRating
                        )
                    }
                }

                is ApiResult.ApiLoading -> {
                    listOf(PopularMoviesItem.Loading)
                }

                is ApiResult.ApiException -> {
                    listOf(PopularMoviesItem.Error)
                }
            }
        }


    }


}