package com.raymond.randomexercise.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.raymond.randomexercise.R;
import com.raymond.randomexercise.ui.widgets.ScrollingImageRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raymond on 2016-04-26.
 */
public class ScrollingImageFragment extends Fragment {

    ScrollingImageRecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scrolling_image, container, false);

        recyclerView = (ScrollingImageRecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(getImageAdapter());


        return v;
    }

    private ImageAdapter getImageAdapter() {
        ArrayList<ImageHolder> list = new ArrayList<>();

        list.add(new ImageHolder(500, 575, getUrl(R.raw._0)));
        list.add(new ImageHolder(500, 575, getUrl(R.raw._1)));
        list.add(new ImageHolder(500, 575, getUrl(R.raw._2)));
        list.add(new ImageHolder(500, 575, getUrl(R.raw._3)));
        list.add(new ImageHolder(500, 575, getUrl(R.raw._4)));
        list.add(new ImageHolder(500, 575, getUrl(R.raw._5)));
        list.add(new ImageHolder(500, 575, getUrl(R.raw._6)));
        list.add(new ImageHolder(500, 575, getUrl(R.raw._7)));
        list.add(new ImageHolder(500, 575, getUrl(R.raw._8)));
        list.add(new ImageHolder(500, 575, getUrl(R.raw._9)));

        return new ImageAdapter(list);
    }

    private String getUrl(int resId) {
        return new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build()
                .toString();
    }



    private static class ImageAdapter extends RecyclerView.Adapter {
        private static final int REPEAT_MAX = 1000;

        List<ImageHolder> imageRes;
        int size;

        public ImageAdapter(List<ImageHolder> imageRes) {
            this.imageRes = imageRes;
            size = imageRes.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
            return new ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageViewHolder vh = (ImageViewHolder) holder;
            int loc;
            if (position >= size) {
                loc = position % size;
            } else {
                loc = position;
            }

            ImageHolder imageHolder = imageRes.get(loc);
            ViewGroup.LayoutParams params = vh.simpleDraweeView.getLayoutParams();
            params.height = imageHolder.height * 2;
            vh.simpleDraweeView.setLayoutParams(params);
            vh.simpleDraweeView.setImageURI(Uri.parse(imageHolder.path));
        }

        @Override
        public int getItemCount() {
            return REPEAT_MAX;
        }
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView simpleDraweeView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.image);
        }
    }

    private static class ImageHolder {
        public int width, height;
        public String path;

        public ImageHolder(int w, int h, String path) {
            width = w;
            height = h;
            this.path = path;
        }
    }
}