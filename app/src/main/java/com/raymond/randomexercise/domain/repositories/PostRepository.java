package com.raymond.randomexercise.domain.repositories;

import com.raymond.randomexercise.domain.db.model.PostItem;
import com.raymond.randomexercise.domain.db.repositories.DBPostRepository;
import com.raymond.randomexercise.domain.network.ApiServiceManager;

import java.util.List;

import rx.Observable;

/**
 * Created by Raymond on 2016-04-14.
 */
public class PostRepository {
    private ApiServiceManager manager;
    private DBPostRepository dbPostRepository;

    public PostRepository(ApiServiceManager manager, DBPostRepository repository) {
        this.manager = manager;
        this.dbPostRepository = repository;
    }

    public Observable<PostItem> getPostListItem(String section, String page) {
        Observable<PostItem> network = manager.getPostListItem(section, page);
        Observable<PostItem> disk = dbPostRepository.getPostListItem(section, page);
        return Observable.concat(disk, network).first();
    }
}
