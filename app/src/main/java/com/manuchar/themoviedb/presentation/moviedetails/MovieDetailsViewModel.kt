package com.manuchar.themoviedb.presentation.moviedetails

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.manuchar.themoviedb.domain.interactors.GetSimilarMoviesUseCase
import com.manuchar.themoviedb.domain.model.MovieModel
import com.manuchar.themoviedb.presentation.base.BaseViewModel
import com.manuchar.themoviedb.presentation.moviedetails.model.DetailArgs
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface MovieDetailsViewModel {

    interface Input {

    }

    interface Output {
        val arguments: StateFlow<DetailArgs>
        val similarMovies: Flow<PagingData<MovieModel>>
    }

    class ViewModel @AssistedInject constructor(
        @Assisted private val args: DetailArgs,
        getSimilarMoviesUseCase: GetSimilarMoviesUseCase
    ) : BaseViewModel(), Input, Output {

        val input: Input = this
        val out: Output = this


        private val _arguments = MutableStateFlow(args)
        override val arguments: StateFlow<DetailArgs> = _arguments.asStateFlow()

        override val similarMovies: Flow<PagingData<MovieModel>> =
            getSimilarMoviesUseCase
                .invoke(args.id)
                .cachedIn(viewModelScope)


        @AssistedFactory
        interface Factory {
            fun create(detailArgs: DetailArgs): ViewModel
        }

        @Suppress("UNCHECKED_CAST")
        companion object {
            fun provideFactory(
                assistedFactory: Factory, detailArgs: DetailArgs
            ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {

                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(detailArgs) as T
                }

            }
        }

    }

}