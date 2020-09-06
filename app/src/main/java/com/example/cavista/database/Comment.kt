package com.example.cavista.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Comment model class for Comments
 */

@Entity(tableName = "comments_table")
class Comment(

    @PrimaryKey(autoGenerate = true)
    var commentId: Long = 0L,

    @ColumnInfo(name = "image_id")
    var imageId: String = "id",

    @ColumnInfo(name = "comment_data")
    var commentData: String = "comment",

    @ColumnInfo(name = "comment_time")
    var commentTime: Long = System.currentTimeMillis()
)
