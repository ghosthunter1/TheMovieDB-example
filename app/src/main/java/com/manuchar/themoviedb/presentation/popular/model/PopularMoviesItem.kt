package com.manuchar.themoviedb.presentation.popular.model

sealed class PopularMoviesItem(val itemType: Int) {

    enum class Types() {
        ITEM, LOADING, ERROR
    }

    data class Item(
        val id: Long,
        val overview: String,
        val imageUrl: String?,
        val name: String,
        val rating: Double
    ) : PopularMoviesItem(Types.ITEM.ordinal)

    object Loading : PopularMoviesItem(Types.LOADING.ordinal)
    object Error : PopularMoviesItem(Types.ERROR.ordinal)

}