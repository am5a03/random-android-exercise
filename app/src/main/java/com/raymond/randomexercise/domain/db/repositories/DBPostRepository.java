package com.raymond.randomexercise.domain.db.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.raymond.randomexercise.domain.db.model.PostItem;
import com.raymond.randomexercise.domain.network.model.ApiResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by Raymond on 2016-04-14.
 */
public class DBPostRepository {
    private static final String TAG = "DBPostRepository";

    private Gson gson;
    private SharedPreferences sharedPreferences;
    private PublishSubject<String> paging = PublishSubject.create();

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

    public Observable<ArrayList<PostItem>> getPostList(String section, String page, boolean remote, PublishSubject pagerSubject) {
        long ts = System.currentTimeMillis();
        String cachedResp = sharedPreferences.getString(formatListKey(section, page), null);
        Log.d(TAG, "getPostList: diff=" + (System.currentTimeMillis() - ts));

        ApiResponse response = gson.fromJson(cachedResp, ApiResponse.class);

        if (response == null || remote) return Observable.never();

        return Observable.just(response)
                .doOnNext(response1 -> {
                    Log.d(TAG, "getPostList: ");
                    if (pagerSubject != null) {
                        pagerSubject.onNext(response1.paging.next);
                    }
                })
                .map(apiResponse -> apiResponse.data)
                .map(apiPostArray -> {
                    Log.d(TAG, "getPostList: ");
                    ArrayList<PostItem> items = new ArrayList<>();
                    for (int i = 0; i < apiPostArray.length; i++) {
                        ApiResponse.ApiData apiData = apiPostArray[i];
                        PostItem item = new PostItem();
                        item.caption = apiData.caption;
                        item.imageUrl = apiData.images.large;
                        item.voteCounts = apiData.votes.count;
                        item.commentCounts = apiData.comments.count;
                        items.add(item);
                    }
                    return items;
                });
    }

    public void savePostList(String section, String page, String response) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(formatListKey(section, page), response);
        editor.apply();
    };

    protected String formatListKey(String section , String page) {
        return section + "_" + page;
    }
}
