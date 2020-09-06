package com.example.cavista.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cavista.database.Comment
import com.example.cavista.database.CommentDatabaseDao
import kotlinx.coroutines.*

/**
 * ViewModel for ImageDetails Act
 */
class ImageDetailsViewModel(
    private val database: CommentDatabaseDao,
    application: Application) : AndroidViewModel(application){

    // Create a job to manage all coroutines
    private var viewModelJob = Job()

    /**
     * This method is called when viewModel is destroyed
     * */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel() // this cancels all the jobs when the viewModel is destroyed
    }

    // Defining the scope for coroutine
    // Scope determines what thread the coroutine will run on and also needs to know about the job
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val comment = MutableLiveData<Comment?>()

    fun getAllComments(id: String): LiveData<List<Comment>> {
        return database.getComments(id)
    }

    init {

    }

    fun insertNewComment(comment: Comment) {
        uiScope.launch {
//            val comment = Comment()
            insertComment(comment)
        }
    }

    private suspend fun insertComment(comment: Comment) {
        withContext(Dispatchers.IO){
            database.insertComment(comment)
        }
    }


}