package com.example.cavista.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cavista.database.CommentDatabaseDao
import java.lang.IllegalArgumentException

/**
 * ViewModel Factory for ImageDetailsViewModel
 */
class ImageDetailsViewModelFactory(
    private val dataSource: CommentDatabaseDao,
    private val application: Application): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageDetailsViewModel::class.java)) {
            return ImageDetailsViewModel(dataSource,application) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}