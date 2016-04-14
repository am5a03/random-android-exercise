package com.raymond.randomexercise.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raymond.randomexercise.R;
import com.raymond.randomexercise.domain.db.model.PostItem;
import com.raymond.randomexercise.domain.db.repositories.DBPostRepository;
import com.raymond.randomexercise.domain.network.ApiServiceManager;
import com.raymond.randomexercise.domain.repositories.PostRepository;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Raymond on 2016-04-15.
 */
public class PostListActivity extends AppCompatActivity {

    private List<PostItem> postItemList;
    private ApiServiceManager serviceManager;
    private DBPostRepository dbPostRepository;
    private PostRepository postRepository;

    private PostListAdapter postListAdapter;

    private Subscription postItemObservable;

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

        postItemObservable = postRepository.getPostListItem("hot", "")
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(postItem -> {
            postItemList.add(postItem);
            postListAdapter.notifyItemInserted(postItemList.size() - 1);
        });
    }

    @Override
    protected void onDestroy() {
        postItemObservable.unsubscribe();
        super.onDestroy();
    }

    private static class PostListAdapter extends RecyclerView.Adapter<PostViewHolder> {

        private List<PostItem> postItems;

        private PostListAdapter(List<PostItem> postItems) {
            this.postItems = postItems;
        }

        @Override
        public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
            return new PostViewHolder(v);
        }

        @Override
        public void onBindViewHolder(PostViewHolder holder, int position) {
            PostItem item = postItems.get(position);
            holder.caption.setText(item.caption);
            holder.image.setText(item.imageUrl);
            holder.voteCount.setText(String.valueOf(item.voteCounts));
        }

        @Override
        public int getItemCount() {
            return postItems.size();
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
