package com.manuchar.themoviedb.presentation.popular

import androidx.lifecycle.viewModelScope
import com.manuchar.themoviedb.utlis.ApiResult
import com.manuchar.themoviedb.domain.interactors.GetPopularMoviesUseCase
import com.manuchar.themoviedb.domain.model.MovieModel
import com.manuchar.themoviedb.presentation.base.BaseViewModel
import com.manuchar.themoviedb.presentation.popular.model.PopularMoviesItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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

    @OptIn(ExperimentalCoroutinesApi::class)
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
                    managePaging(result)
                    _popularMovies.getAndUpdate {
                        it.dropLast(1)
                            .plus(buildList(result))
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

        private fun managePaging(apiResult: ApiResult<List<MovieModel>>) {
            apiResult.onSuccess {
                if (it.size == PAGE_SIZE) {
                    isLoading = false
                    currentPage += 1
                }
            }

            apiResult.onLoading {
                isLoading = true
            }
        }

        private fun buildList(moviesResponse: ApiResult<List<MovieModel>>): List<PopularMoviesItem> {

            return when (moviesResponse) {
                is ApiResult.ApiSuccess -> {
                    moviesResponse.data.map { item ->
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

    companion object {
        const val PAGE_SIZE = 20
    }


}