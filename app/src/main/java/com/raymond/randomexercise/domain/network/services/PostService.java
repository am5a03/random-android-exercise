package com.raymond.randomexercise.domain.network.services;

import com.raymond.randomexercise.domain.network.model.ApiResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Raymond on 2016-04-14.
 */
public interface PostService {
    @GET("{section}/{page}")
    Observable<ApiResponse> getPostListResponse(@Path("section") String section,
                                                @Path("page") String page);
}
