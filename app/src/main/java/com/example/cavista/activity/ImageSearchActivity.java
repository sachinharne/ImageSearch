package com.example.cavista.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cavista.R;
import com.example.cavista.adapter.ImageAdapter;
import com.example.cavista.model.ImageModelClass;
import com.example.cavista.ui.ImageSearchActivityViewModel;

import java.util.ArrayList;

/**
 * ImageSearchActivity to search the images and show the list on the screen.
 */
public class ImageSearchActivity extends AppCompatActivity implements LifecycleOwner {

    private static final String TAG = "ImageSearchActivity";
    private static int page = 0;
    private static String searchQuery = "";
    private ImageSearchActivityViewModel viewModel;
    private RecyclerView recyclerViewSearch;
    private TextView textView;
    private Observer<ArrayList<ImageModelClass>> userListUpdateObserver = new Observer<ArrayList<ImageModelClass>>() {
        @Override
        public void onChanged(final ArrayList<ImageModelClass> userArrayList) {

            if (userArrayList.size() > 0) {
                textView.setVisibility(View.GONE);
                recyclerViewSearch.setVisibility(View.VISIBLE);

                ImageAdapter adapter = new ImageAdapter();
                recyclerViewSearch.setAdapter(adapter);
                adapter.setImages(userArrayList);
                adapter.setItemClickListener(new ImageAdapter.ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        final Intent intent = new Intent(
                                ImageSearchActivity.this, ImageDetailsActivity.class);
                        intent.putExtra("image", userArrayList.get(position));
                        startActivity(intent);
                    }
                });
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(getResources().getString(R.string.no_results_found));
                recyclerViewSearch.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);

        init();

        viewModel = new ImageSearchActivityViewModel(getApplication(), page, searchQuery);
        viewModel.getImagesLiveData().observe(this,
                userListUpdateObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                page = 1;
                searchQuery = query;

                if (!query.isEmpty()) {
                    viewModel.init(page, query);
                } else {
                    Toast.makeText(ImageSearchActivity.this,
                            R.string.search_warning, Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Initializing parameters
     */
    private void init() {
        textView = findViewById(R.id.textView);
        recyclerViewSearch = findViewById(R.id.recyclerResults);
        recyclerViewSearch.setHasFixedSize(true);
    }
}
