package com.manuchar.themoviedb.di

import com.manuchar.themoviedb.data.respository.MoviesRepositoryImpl
import com.manuchar.themoviedb.domain.repository.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepositoryModule {


    @Binds
    abstract fun provideMoviesRepository(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository

}