package com.example.cavista.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cavista.databinding.LayoutItemImageBinding;
import com.example.cavista.model.ImageModelClass;

import java.util.ArrayList;
import java.util.List;

/**
 * ImageAdapter to show the list of images into RecyclerView
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<ImageModelClass> images;
    private ItemClickListener mItemClickListener;

    public ImageAdapter() {
        images = new ArrayList<>();
    }

    /**
     * Set interface for handling image item clicks
     *
     * @param itemClickListener : click listener interface
     */
    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.bind(images.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onClick(holder.itemView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    /**
     * Set the new image list to adapter
     *
     * @param imagesList new image list to set to adapter
     */
    public void setImages(@NonNull List<ImageModelClass> imagesList) {
        images.clear();
        images.addAll(imagesList);
        notifyDataSetChanged();
    }

    /**
     * ItemCLick listener interface to handle RecyclerItem Clicks
     */
    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    /**
     * ViewHolder for Recycler ItemView
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private LayoutItemImageBinding binding;

        public ViewHolder(LayoutItemImageBinding itemImageBinding) {
            super(itemImageBinding.getRoot());
            binding = itemImageBinding;
        }

        public static ViewHolder from(@NonNull ViewGroup parent) {
            return new ViewHolder(LayoutItemImageBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false));
        }

        /**
         * Bind data to the itemVIew
         *
         * @param image imageData
         */
        public void bind(@NonNull ImageModelClass image) {
            binding.setImageClass(image);
            binding.executePendingBindings();
        }
    }
}
