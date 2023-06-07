package com.manuchar.themoviedb.presentation.moviedetails

import androidx.recyclerview.widget.DiffUtil
import com.manuchar.themoviedb.domain.model.MovieModel

object SimilarMoviesDiffUtils : DiffUtil.ItemCallback<MovieModel>() {

    override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: MovieModel,
        newItem: MovieModel
    ): Boolean {
        return oldItem == newItem
    }


}