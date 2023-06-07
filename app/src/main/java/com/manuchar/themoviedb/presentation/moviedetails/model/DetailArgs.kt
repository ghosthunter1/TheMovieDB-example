package com.manuchar.themoviedb.presentation.moviedetails.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailArgs(
    val id: Long,
    val image: String?,
    val name: String,
    val overview: String,
    val rating: Double
) : Parcelable