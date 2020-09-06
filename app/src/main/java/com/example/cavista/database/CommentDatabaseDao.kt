package com.example.cavista.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * CommentDatabaseDao class
 */

@Dao
interface CommentDatabaseDao {

    @Insert
    fun insertComment(comment: Comment)

    @Query("SELECT * FROM comments_table WHERE image_id = :imageId ORDER BY comment_time DESC")
    fun getComments(imageId: String): LiveData<List<Comment>>
}