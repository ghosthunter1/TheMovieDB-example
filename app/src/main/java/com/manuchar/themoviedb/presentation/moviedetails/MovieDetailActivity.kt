package com.manuchar.themoviedb.presentation.moviedetails

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.manuchar.themoviedb.databinding.ActivityMovieDetailsBinding
import com.manuchar.themoviedb.presentation.base.BaseActivity
import com.manuchar.themoviedb.presentation.moviedetails.model.DetailArgs
import com.manuchar.themoviedb.utlis.SpaceItemDecoration
import com.manuchar.themoviedb.utlis.dp
import com.manuchar.themoviedb.utlis.observeFlow
import com.manuchar.themoviedb.utlis.drawFromUrl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailActivity : BaseActivity<ActivityMovieDetailsBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMovieDetailsBinding
        get() = ActivityMovieDetailsBinding::inflate

    @Inject
    lateinit var factory: MovieDetailsViewModel.ViewModel.Factory


    private val viewModel: MovieDetailsViewModel.ViewModel by viewModels {
        val args = intent.getParcelableExtra<DetailArgs>(ARGS)!!
        MovieDetailsViewModel.ViewModel.provideFactory(factory, args)
    }

    private val similarMoviesAdapter = SimilarMoviesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpViews()
        setUpObservers()
        setUpListeners()
    }

    private fun setUpViews() {
        views.similarMoviesList.addItemDecoration(SpaceItemDecoration(10.dp, 10.dp, 16.dp, 16.dp))
        views.similarMoviesList.adapter = similarMoviesAdapter
    }

    private fun setUpObservers() {
        viewModel.out.arguments.observeFlow(this) {
            setUp(it)
        }

        viewModel.out.similarMovies.observeFlow(this) {
            similarMoviesAdapter.submitData(it)
        }

    }

    private fun setUp(args: DetailArgs) = with(args) {
        image?.let {
            views.movieImage.drawFromUrl(image)
        }
        views.averageRating.text = rating.toString()
        views.movieName.text = name
        views.overviewText.text = overview
    }

    private fun setUpListeners() {

        similarMoviesAdapter.setCallBack {
            start(
                this,
                DetailArgs(it.id, it.imageUrl, it.name, it.overview, it.averageRating)
            )
        }
    }

    companion object {

        private const val ARGS = "ARGS"

        fun start(
            context: Context,
            detailArgs: DetailArgs,
            options: ActivityOptions? = null
        ) {
            context
                .startActivity(
                    Intent(context, MovieDetailActivity::class.java)
                        .apply {
                            putExtra(ARGS, detailArgs)
                        }, options?.toBundle()
                )
        }
    }
}