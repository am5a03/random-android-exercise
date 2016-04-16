package com.raymond.randomexercise.domain.repositories;

import com.raymond.randomexercise.domain.db.model.PostItem;
import com.raymond.randomexercise.domain.db.repositories.DBPostRepository;
import com.raymond.randomexercise.domain.network.ApiServiceManager;
import com.raymond.randomexercise.domain.network.model.ApiResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.schedulers.Timestamped;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by Raymond on 2016-04-14.
 */
public class PostRepository {
    private PublishSubject<String> nextPageSubj = PublishSubject.create();
    private ApiServiceManager manager;
    private DBPostRepository dbPostRepository;

    public PostRepository(ApiServiceManager manager, DBPostRepository repository) {
        this.manager = manager;
        this.dbPostRepository = repository;
    }

    public Observable<PostItem> getPostListItem(String section, String page) {
        Observable<PostItem> network = manager.getPostListItem(section, page);
        Observable<PostItem> disk = dbPostRepository.getPostListItem(section, page);
        return Observable.amb(disk, network);
    }

    public Observable<ArrayList<PostItem>> getPostList(String section, String page, boolean remote, PublishSubject<String> pagerSubject) {
        Observable<ArrayList<PostItem>> network = manager.getPostList(section, page, pagerSubject);
        Observable<ArrayList<PostItem>> disk = dbPostRepository.getPostList(section, page, remote, pagerSubject);
        return Observable.amb(disk, network);
    }

}
