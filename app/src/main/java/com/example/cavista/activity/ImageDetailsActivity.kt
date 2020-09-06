package com.example.cavista.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cavista.R
import com.example.cavista.adapter.CommentAdapter
import com.example.cavista.database.Comment
import com.example.cavista.database.CommentDatabase
import com.example.cavista.databinding.ActivityImageDetailsBinding
import com.example.cavista.model.ImageModelClass
import com.example.cavista.ui.ImageDetailsViewModel
import com.example.cavista.ui.ImageDetailsViewModelFactory

/**
 * ImageDetails Activity for showing image details and comments
 **/
class ImageDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageDetailsBinding
    private lateinit var imageId: String
    private lateinit var title: String
    private lateinit var imagePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_details)

        init()
    }

    private fun init() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val dataSource = CommentDatabase
            .getInstance(this).commentDatabaseDao

        val viewModelFactory = ImageDetailsViewModelFactory(dataSource, application)

        val imageDetailsViewModel =
            ViewModelProvider(this, viewModelFactory).get(ImageDetailsViewModel::class.java)

        binding.detailsViewModel = imageDetailsViewModel

        binding.lifecycleOwner = this

        if (intent.extras != null) {
            val image = intent.extras!!.getParcelable<ImageModelClass>("image")
            if (image != null) {
                imageId = image.id
                title = image.name
                imagePath = image.imagePath
                Glide.with(this)
                    .load(imagePath)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_broken_image)
                    )
                    .into(binding.imageView2)
                supportActionBar?.title = image.name
            }
        }

        val adapter = CommentAdapter()
        binding.commentsList.adapter = adapter

        imageDetailsViewModel.getAllComments(imageId).observe(this, {
            Log.d(TAG, "onCreate: size " + it.size)

            it.let {
                adapter.comments = it
            }
        })

        binding.submitButton.setOnClickListener(View.OnClickListener {
            val comment = binding.editTextComment.text.toString()

            if (comment.isEmpty()) {
                Toast.makeText(this, "Comments can't be empty.", Toast.LENGTH_SHORT).show()
                binding.editTextComment.requestFocus()
                return@OnClickListener
            }

            val commentData = Comment()
            commentData.commentData = comment
            commentData.imageId = imageId
            imageDetailsViewModel.insertNewComment(commentData)

            binding.editTextComment.text.clear()
            it.hideKeyboard()
        })
    }

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return false
    }

    companion object {
        private const val TAG = "ImageDetailsActivity"
    }
}
