package com.manuchar.themoviedb.presentation.moviedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.manuchar.themoviedb.databinding.ListItemSimilarMovieBinding
import com.manuchar.themoviedb.domain.model.MovieModel
import com.manuchar.themoviedb.utlis.dp
import com.manuchar.themoviedb.utlis.drawFromUrl

class SimilarMoviesAdapter :
    PagingDataAdapter<MovieModel, RecyclerView.ViewHolder>(SimilarMoviesDiffUtils) {

    private var callBack: CallBack? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return SimilarMovieViewHolder(
            ListItemSimilarMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SimilarMovieViewHolder -> {
                getItem(position)?.let {
                    holder.bind(it)
                }
            }
        }
    }


    inner class SimilarMovieViewHolder(private val binding: ListItemSimilarMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieModel) = with(binding) {
            movie.poster?.let {
                image.drawFromUrl(it,5.dp)
            }
            nameText.text = movie.name
            ratingText.text = movie.averageRating.toString()

            root.setOnClickListener {
                callBack?.onClick(movie)
            }
        }
    }

    fun interface CallBack {
        fun onClick(movie: MovieModel)
    }
}