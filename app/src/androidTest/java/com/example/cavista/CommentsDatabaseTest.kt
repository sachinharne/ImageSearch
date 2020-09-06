package com.example.cavista

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.cavista.database.Comment
import com.example.cavista.database.CommentDatabase
import com.example.cavista.database.CommentDatabaseDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * This is not meant to be a full set of tests. For simplicity, most of samples do not include
 * tests. However when building the Room, it is helpful to make sure it works before adding it
 * to the UI.
 */

@RunWith(AndroidJUnit4::class)
class CommentsDatabaseTest {

    private lateinit var commentDao: CommentDatabaseDao
    private lateinit var database: CommentDatabase

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using in-memory database because the information store here disappears when the process
        // is killed
        database = Room.inMemoryDatabaseBuilder(context, CommentDatabase::class.java)
            // Allowing the main thread queries, just for testing
            .allowMainThreadQueries()
            .build()

        commentDao = database.commentDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    @Test
    @Throws(IOException::class)
    fun insertAndGetComment() {
        val comment = Comment()
        commentDao.insertComment(comment)
        commentDao.getComments(comment.imageId)
    }
}
