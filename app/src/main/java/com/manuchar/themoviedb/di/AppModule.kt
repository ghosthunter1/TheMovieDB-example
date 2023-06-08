package com.manuchar.themoviedb.di

import com.manuchar.themoviedb.BuildConfig
import com.manuchar.themoviedb.data.api.MoviesApi
import com.manuchar.themoviedb.utlis.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    @Singleton
    @Provides
    fun provideRetrofitHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original
                    .newBuilder()
                    .header(
                        "Authorization",
                        BuildConfig.API_KEY
                    )
                    .method(original.method, original.body)
                    .build()
                return@addInterceptor chain.proceed(request)
            }
            .addInterceptor(interceptor).build()
    }


    @Provides
    @IoDispatcher
    fun provideCoroutineDispatcher(): CoroutineContext = Dispatchers.IO

    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideMoviesApi(retrofit: Retrofit): MoviesApi = retrofit.create(MoviesApi::class.java)


}