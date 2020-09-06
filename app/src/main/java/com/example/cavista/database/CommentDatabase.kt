package com.example.cavista.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * CommentDatabase class
 */

@Database(entities = [Comment::class], version = 1, exportSchema = false)
abstract class CommentDatabase : RoomDatabase() {

    abstract val commentDatabaseDao: CommentDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: CommentDatabase? = null

        fun getInstance(context: Context): CommentDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CommentDatabase::class.java,
                        "comments_table"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }
    }

}