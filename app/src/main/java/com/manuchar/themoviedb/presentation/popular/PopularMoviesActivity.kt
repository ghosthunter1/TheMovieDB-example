package com.manuchar.themoviedb.presentation.popular

import android.app.ActivityOptions
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import com.manuchar.themoviedb.utlis.observeFlows
import com.manuchar.themoviedb.databinding.ActivityPopularMoviesBinding
import com.manuchar.themoviedb.presentation.base.BaseActivity
import com.manuchar.themoviedb.presentation.moviedetails.MovieDetailActivity
import com.manuchar.themoviedb.presentation.moviedetails.model.DetailArgs
import com.manuchar.themoviedb.presentation.popular.model.PopularMoviesItem
import com.manuchar.themoviedb.utlis.SpaceItemDecoration
import com.manuchar.themoviedb.utlis.dp
import com.manuchar.themoviedb.utlis.lastItemReached
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularMoviesActivity : BaseActivity<ActivityPopularMoviesBinding>() {


    override val bindingInflater: (LayoutInflater) -> ActivityPopularMoviesBinding
        get() = ActivityPopularMoviesBinding::inflate

    private val viewModel: PopularMoviesViewModel.ViewModel by viewModels()

    private var adapter = PopularMoviesAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpViews()
        setUpObservers()
        setUpListeners()

    }

    private fun setUpViews() {
        views.popularMoviesList.adapter = adapter
        views.popularMoviesList.addItemDecoration(SpaceItemDecoration(16.dp))
    }

    private fun setUpObservers() {
        viewModel.out.popularMovies.observeFlows(this) {
            adapter.submitList(it)
        }

    }

    private fun setUpListeners() {

        adapter.setCallBack(object : PopularMoviesAdapter.CallBack {

            override fun onItemClick(
                item: PopularMoviesItem.Item,
                pairs: Array<Pair<View, String>>
            ) {
                startDetailActivity(item, pairs)
            }

            override fun retry() {
                viewModel.input.retry()
            }

        })


        views.popularMoviesList.lastItemReached {
            viewModel.input.fetchMore()
        }


    }

    private fun startDetailActivity(
        item: PopularMoviesItem.Item,
        pairs: Array<Pair<View, String>>
    ) {
        val options = ActivityOptions
            .makeSceneTransitionAnimation(
                this@PopularMoviesActivity,
                *pairs
            )

        MovieDetailActivity.start(
            this@PopularMoviesActivity,
            DetailArgs(item.id, item.imageUrl, item.name, item.overview, item.rating),
            options
        )
    }

}