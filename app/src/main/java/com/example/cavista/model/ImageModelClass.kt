package com.example.cavista.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ImageModelClass to represent an image
 */

@Parcelize
data class ImageModelClass(
        val id: String,
        val name: String,
        val imagePath: String
) : Parcelable
