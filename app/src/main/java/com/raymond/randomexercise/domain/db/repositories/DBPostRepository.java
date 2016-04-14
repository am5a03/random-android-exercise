package com.raymond.randomexercise.domain.db.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.raymond.randomexercise.domain.db.model.PostItem;
import com.raymond.randomexercise.domain.network.model.ApiResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by Raymond on 2016-04-14.
 */
public class DBPostRepository {
    private static final String TAG = "DBPostRepository";

    private Gson gson;
    private SharedPreferences sharedPreferences;

    public DBPostRepository(Context context) {
        sharedPreferences = context.getSharedPreferences("com.raymond.randomexercise.API_KEY", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public Observable<PostItem> getPostListItem(String section, String page) {
        String cachedResp = sharedPreferences.getString(formatListKey(section, page), null);
        ApiResponse response = gson.fromJson(cachedResp, ApiResponse.class);

        if (response == null) return Observable.empty();

        return Observable.just(response)
                .map(apiResponse -> apiResponse.data)
                .flatMap(Observable::from)
                .flatMap(apiData -> {
                    Log.d(TAG, "getPostListItem: ");
                    PostItem item = new PostItem();
                    item.caption = apiData.caption;
                    item.imageUrl = apiData.images.large;
                    item.voteCounts = apiData.votes.count;
                    item.commentCounts = apiData.comments.count;
                    return Observable.just(item);
                });
    };

    public void savePostList(String section, String page, String response) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(formatListKey(section, page), response);
        editor.apply();
    };

    protected String formatListKey(String section , String page) {
        return section + "_" + page;
    }
}
