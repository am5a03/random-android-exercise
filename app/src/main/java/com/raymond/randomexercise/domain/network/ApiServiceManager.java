package com.raymond.randomexercise.domain.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.raymond.randomexercise.domain.db.model.PostItem;
import com.raymond.randomexercise.domain.db.repositories.DBPostRepository;
import com.raymond.randomexercise.domain.network.model.ApiResponse;
import com.raymond.randomexercise.domain.network.services.PostService;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by Raymond on 2016-04-14.
 */
public class ApiServiceManager {
    private static final String TAG = "ApiServiceManager";
    private Gson gson;
    private PostService postService;

    private PublishSubject<String> nextPageSubject = PublishSubject.create();
    private DBPostRepository repository;

    public ApiServiceManager(DBPostRepository repository) {

        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://infinigag.k3min.eu")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        postService = retrofit.create(PostService.class);
        gson = new Gson();

        this.repository = repository;
    }


    public Observable<ArrayList<PostItem>> getPostList(String section, String page, PublishSubject<String> pagerSubject) {
        return postService.getPostListResponse(section, page)
                .doOnNext(apiResponse -> {
                    Log.d(TAG, "getPostList: " + pagerSubject);
                    if (pagerSubject != null) {
                        pagerSubject.onNext(apiResponse.paging.next);
                    }
                    String savedResp = gson.toJson(apiResponse);
                    // Check whether
                    repository.savePostList(section, page, savedResp);

                })
                .doOnNext(response -> nextPageSubject.onNext(response.paging.next))
                .map(apiResponse -> apiResponse.data)
                .map(apiPostArray -> {
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
                })
                .onErrorResumeNext(throwable -> {
                    Log.e(TAG, "getPostList: ", throwable);
                    return repository.getPostList(section, page, false, pagerSubject);
                });
    }


    public Observable<PostItem> getPostListItem(String section, String page) {

        return postService.getPostListResponse(section, page)
                .doOnNext(apiResponse -> {
                    String savedResp = gson.toJson(apiResponse);
                    repository.savePostList(section, page, savedResp);
                })
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
    }

}
