package com.raymond.randomexercise.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.raymond.randomexercise.R;
import com.raymond.randomexercise.domain.db.model.PostItem;
import com.raymond.randomexercise.domain.db.repositories.DBPostRepository;
import com.raymond.randomexercise.domain.network.ApiServiceManager;
import com.raymond.randomexercise.domain.repositories.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Raymond on 2016-04-15.
 */
public class PostListActivity extends AppCompatActivity {
    private static final String TAG = "PostListActivity";
    private List<PostItem> postItemList;
    private ApiServiceManager serviceManager;
    private DBPostRepository dbPostRepository;
    private PostRepository postRepository;

    private PostListAdapter postListAdapter;

    private Subscription postItemObservable;
    private Subscription postListObservable;

    private RecyclerViewPagerListener pagerListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);

        postItemList = new ArrayList<>();
        dbPostRepository = new DBPostRepository(this);
        serviceManager = new ApiServiceManager(dbPostRepository);
        postRepository = new PostRepository(serviceManager, dbPostRepository);

        postListAdapter = new PostListAdapter(postItemList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if (recyclerView == null) return;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postListAdapter);


        SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.srl);
        pagerListener = new RecyclerViewPagerListener(postItemList, recyclerView, srl, postRepository);
        pagerListener.init();
        recyclerView.addOnScrollListener(pagerListener);

        assert srl != null;
        srl.setOnRefreshListener(() -> {
            postRepository.getPostList("hot", "", true, pagerListener.getPagerSubject())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(postItems -> {
                        postItemList.clear();
                        for (PostItem postItem : postItems) {
                            postItemList.add(postItem);
                        }
                        postListAdapter.notifyDataSetChanged();
                        srl.setRefreshing(false);
                    });
        });

    }

    @Override
    protected void onDestroy() {
        pagerListener.destroy();
        super.onDestroy();
    }

    private static class PostListAdapter extends RecyclerView.Adapter {

        private List<PostItem> postItems;
        private static final int TYPE_PROGRESS = 0;
        private static final int TYPE_ITEM = 1;

        private PostListAdapter(List<PostItem> postItems) {
            this.postItems = postItems;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_PROGRESS:
                    View pv = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
                    return new ProgressViewHolder(pv);
                default:
                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
                    return new PostViewHolder(v);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ProgressViewHolder) {
                ProgressViewHolder pvh = (ProgressViewHolder) holder;
            } else if (holder instanceof PostViewHolder){
                PostViewHolder pv = (PostViewHolder) holder;
                PostItem item = postItems.get(position);
                pv.caption.setText(item.caption);
                pv.image.setText(item.imageUrl);
                pv.voteCount.setText(String.valueOf(item.voteCounts));
            }
        }

        int mapPositionToItem(int position) {
            return position - 1;
        }

        @Override
        public int getItemCount() {
            return postItems.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == postItems.size()) {
                return TYPE_PROGRESS;
            } else {
                return TYPE_ITEM;
            }
        }
    }

    private static class RecyclerViewPagerListener extends RecyclerView.OnScrollListener {
        private String nextPage;
        private PublishSubject<String> pagerSubject = PublishSubject.create();
        private PostRepository postRepository;
        private List list;
        private RecyclerView view;
        private SwipeRefreshLayout srl;

        private CompositeSubscription subscription;

        private RecyclerViewPagerListener(List list,
                                          RecyclerView view,
                                          SwipeRefreshLayout srl,
                                          PostRepository repository) {
            this.list = list;
            this.postRepository = repository;
            this.view = view;
            this.nextPage = "";
            this.srl = srl;
            subscription = new CompositeSubscription();
        }

        public void init() {
            subscription.add(pagerSubject.doOnNext(s -> {
                nextPage = s;
                Log.d(TAG, "onScrollStateChanged: " + nextPage);
            })
            .subscribe());
            subscription.add(postRepository.getPostList("hot", nextPage, false, pagerSubject)
                    .flatMap(Observable::from)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(postItem -> {
                        list.add(postItem);
                        view.getAdapter().notifyItemInserted(list.size() + 1);
                        srl.setRefreshing(false);
                    }));
        }

        public void destroy() {
            //
            subscription.unsubscribe();
        }

        public PublishSubject<String> getPagerSubject() {
            return pagerSubject;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int first = manager.findFirstVisibleItemPosition();
            int last = manager.findLastVisibleItemPosition();

            if (last + 3 >= list.size()) {
                subscription.add(postRepository.getPostList("hot", nextPage, false, pagerSubject)
                        .debounce(1500, TimeUnit.MILLISECONDS)
                        .flatMap(Observable::from)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(postItem -> {
                            list.add(postItem);
                            recyclerView.getAdapter().notifyItemInserted(list.size() + 1);
                            srl.setRefreshing(false);
                        }));
            }
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    private static class PostViewHolder extends RecyclerView.ViewHolder {

        public TextView caption;
        public TextView image;
        public TextView voteCount;

        public PostViewHolder(View itemView) {
            super(itemView);
            caption = (TextView) itemView.findViewById(R.id.caption);
            image = (TextView) itemView.findViewById(R.id.image);
            voteCount = (TextView) itemView.findViewById(R.id.voteCount);
        }
    }
}
