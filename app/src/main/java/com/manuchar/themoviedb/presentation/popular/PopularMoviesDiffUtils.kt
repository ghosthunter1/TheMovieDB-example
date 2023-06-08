package com.manuchar.themoviedb.presentation.popular

import androidx.recyclerview.widget.DiffUtil
import com.manuchar.themoviedb.presentation.popular.model.PopularMoviesItem

object PopularMoviesDiffUtils : DiffUtil.ItemCallback<PopularMoviesItem>() {

    override fun areItemsTheSame(
        oldItem: PopularMoviesItem,
        newItem: PopularMoviesItem
    ) = oldItem == newItem


    override fun areContentsTheSame(
        oldItem: PopularMoviesItem,
        newItem: PopularMoviesItem
    ): Boolean {
        if (oldItem is PopularMoviesItem.Item && newItem is PopularMoviesItem.Item) {
            return oldItem.id == newItem.id
        }
        return oldItem == newItem
    }


}