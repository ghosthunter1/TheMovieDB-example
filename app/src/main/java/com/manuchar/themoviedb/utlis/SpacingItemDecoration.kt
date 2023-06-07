package com.manuchar.themoviedb.utlis

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class SpaceItemDecoration(
    private val top: Int = 0,
    private val bottom: Int = 0,
    private val start: Int = 0,
    private val end: Int = 0
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = start
        outRect.right = end
        outRect.bottom = bottom
        outRect.top = top

    }
}