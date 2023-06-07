package com.manuchar.themoviedb.presentation.popular

import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.manuchar.themoviedb.databinding.ListItemErrorBinding
import com.manuchar.themoviedb.databinding.ListItemLoadingBinding
import com.manuchar.themoviedb.databinding.ListItemPopularMovieBinding
import com.manuchar.themoviedb.presentation.popular.model.PopularMoviesItem
import com.manuchar.themoviedb.utlis.dp
import com.manuchar.themoviedb.utlis.drawFromUrl
import java.lang.Exception

class PopularMoviesAdapter : ListAdapter<PopularMoviesItem, ViewHolder>(PopularMoviesDiffUtils) {


    private var callBack: CallBack? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {

            PopularMoviesItem.Types.ITEM.ordinal -> PopularMovieViewHolder(
                ListItemPopularMovieBinding.inflate(inflater, parent, false)
            )

            PopularMoviesItem.Types.LOADING.ordinal -> LoadingViewHolder(
                ListItemLoadingBinding.inflate(inflater, parent, false)
            )

            PopularMoviesItem.Types.ERROR.ordinal -> ErrorViewHolder(
                ListItemErrorBinding.inflate(inflater, parent, false)
            )

            else -> {
                throw Exception("INVALID VIEW TYPE")
            }
        }
    }

    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)!!.itemType
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is PopularMovieViewHolder -> {
                holder.bind(getItem(position) as PopularMoviesItem.Item)
            }
        }
    }


    inner class PopularMovieViewHolder(private val binding: ListItemPopularMovieBinding) :
        ViewHolder(binding.root) {


        private val arrayOfPairs = arrayOf<Pair<View, String>>(
            Pair(binding.movieImage, "image"),
            Pair(binding.movieName, "name"),
            Pair(binding.averageRatingText, "rating_text"),
            Pair(binding.averageRating, "rating")
        )

        fun bind(item: PopularMoviesItem.Item) = with(binding) {
            item.imageUrl?.let {
                movieImage.drawFromUrl(it, 5.dp)
            }
            movieName.text = item.name
            averageRating.text = item.rating.toString()

            binding.root.setOnClickListener {
                callBack?.onItemClick(item, arrayOfPairs)
            }

        }

    }

    inner class ErrorViewHolder(private val binding: ListItemErrorBinding) :
        ViewHolder(binding.root) {

        init {
            binding.retryBtn.setOnClickListener {
                callBack?.retry()
            }
        }

    }

    inner class LoadingViewHolder(binding: ListItemLoadingBinding) : ViewHolder(binding.root)

    interface CallBack {
        fun onItemClick(
            item: PopularMoviesItem.Item, pairs: Array<Pair<View, String>>
        )

        fun retry()
    }
}