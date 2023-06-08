package com.manuchar.themoviedb.utlis

import android.content.res.Resources
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


fun <T> Flow<T>.observeFlow(
    lifecycleOwner: LifecycleOwner,
    consumer: suspend (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collectLatest {
                consumer.invoke(it)
            }
        }
    }
}

fun <T> Flow<T>.toRequestedResult(): Flow<ApiResult<T>> {
    return map {
        ApiResult.ApiSuccess(it)
    }.catch<ApiResult<T>> {
        emit(ApiResult.ApiException(it))
    }.onStart { emit(ApiResult.ApiLoading()) }
}

fun ImageView.drawFromUrl(
    url: String,
    corners: Int? = null
) {
    val glide = Glide.with(context)
        .load(url)
        .transform(CenterCrop())
    corners?.let {
        glide.transform(RoundedCorners(it))
    }
    glide.into(this)

}

val Int.dp: Int
    get() = (this * Resources.getSystem()
        .displayMetrics.density)
        .toInt()


fun RecyclerView.lastItemReached(consumer: () -> Unit) {

    var pastVisibleItems: Int
    var visibleItemCount: Int
    var totalItemCount: Int


    addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            if (dy > 0) {
                visibleItemCount = recyclerView.layoutManager!!.childCount
                totalItemCount = recyclerView.layoutManager!!.itemCount
                pastVisibleItems =
                    (recyclerView.layoutManager!! as LinearLayoutManager).findFirstVisibleItemPosition()

                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    consumer.invoke()
                }
            }
        }
    })
}