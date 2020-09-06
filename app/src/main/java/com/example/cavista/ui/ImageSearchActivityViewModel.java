package com.example.cavista.ui;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cavista.R;
import com.example.cavista.model.ImageModelClass;
import com.example.cavista.utils.SearchApiStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ViewModel for ImageSearchActivity
 */
public class ImageSearchActivityViewModel extends AndroidViewModel {

    private static final String TAG = "ImageSearchActivityView";
    public ArrayList<ImageModelClass> imageResults = new ArrayList<>();
    MutableLiveData<ArrayList<ImageModelClass>> imagesLiveData;

    // The internal MutableLiveData String that stores the status of the most recent request
    private MutableLiveData<SearchApiStatus> _status = new MutableLiveData<SearchApiStatus>();

    private LiveData<SearchApiStatus> status = getStatus();

    public LiveData<SearchApiStatus> getStatus() {
        return _status;
    }

    public ImageSearchActivityViewModel(Application application, final int page, String query) {
        super(application);
        Log.d(TAG, "ImageSearchActivityViewModel: called");
        imagesLiveData = new MutableLiveData<>();

        init(page, query);
    }

    public MutableLiveData<ArrayList<ImageModelClass>> getImagesLiveData() {
        return imagesLiveData;
    }

    public void init(final int page, String query) {
        if (!query.isEmpty()) {
            populateList(page, query);
            imagesLiveData.setValue(imageResults);
        }
    }

    /**
     * This method fetches the images from the API.
     *
     * @param page  : page number
     * @param query : search query
     */
    public void populateList(final int page, String query) {
        Log.d(TAG, "populateList: called");
        final String URL = "https://api.imgur.com/3/gallery/search/" + page + "?q=" + query;

        _status.setValue(SearchApiStatus.LOADING);

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        _status.setValue(SearchApiStatus.DONE);

                        Log.d(TAG, "onResponse: " + response);

                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                // If its a first page i.e new search then clear previous list data
                                if (page == 1) {
                                    imageResults.clear();
                                    imagesLiveData.setValue(imageResults);
                                }
                                final JSONArray data = jsonObject.getJSONArray("data");
                                if (data.length() > 0) {
                                    for (int i = 0; i < data.length(); i++) {
                                        final JSONObject dataObject = data.getJSONObject(i);
                                        final JSONArray imagesArray =
                                                dataObject.optJSONArray("images");

                                        // Sometimes image array is not available
                                        // The image link is given in data itself
                                        if (imagesArray != null) {
                                            for (int j = 0; j < imagesArray.length(); j++) {
                                                final JSONObject imageObject =
                                                        imagesArray.getJSONObject(j);
                                                imageResults.add(new ImageModelClass(
                                                        imageObject.getString("id"),
                                                        dataObject.getString("title"),
                                                        imageObject.getString("link")));
                                            }
                                        } else {
                                            imageResults.add(new ImageModelClass(
                                                    dataObject.getString("id"),
                                                    dataObject.getString("title"),
                                                    dataObject.getString("link")));
                                        }
                                    }
                                    imagesLiveData.setValue(imageResults);
                                }
                            } else {
                                Toast.makeText(
                                        getApplication(),
                                        getApplication().getResources()
                                                .getString(R.string.please_try_again),
                                        Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        _status.setValue(SearchApiStatus.ERROR);
                        Log.e(TAG, "onErrorResponse: error " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Client-ID 137cda6b5008a7c");
                return headers;
            }
        };

        Volley.newRequestQueue(getApplication())
                .add(stringRequest.setRetryPolicy(new DefaultRetryPolicy()));
    }
}
