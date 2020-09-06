package com.example.cavista.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cavista.R
import com.example.cavista.database.Comment
import com.example.cavista.model.ImageModelClass
import com.example.cavista.utils.GetDateTimeFromMillis
import com.example.cavista.utils.SearchApiStatus
import kotlinx.android.synthetic.main.layout_item_image.view.*

/**
 * Binding adapter class for binding data to views
 */

@BindingAdapter("commentData")
fun TextView.setCommentData(comment: Comment) {
    comment.let {
        text = comment.commentData
    }
}

@BindingAdapter("commentTime")
fun TextView.setCommentTime(comment: Comment) {
    comment.let {
        text = GetDateTimeFromMillis.convertToDate(comment.commentTime)
    }
}

@BindingAdapter("imagePath")
fun ImageView.setImagePath(image: ImageModelClass) {
    image.let {
        Glide.with(imageView.context)
            .load(image.imagePath)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imageView)
    }
}

@BindingAdapter("searchApiStatus")
fun bindStatus(statusImageView: ImageView, status: SearchApiStatus?){
    when (status) {
        SearchApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }

        SearchApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }

        SearchApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}