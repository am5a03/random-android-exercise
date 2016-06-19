package com.raymond.randomexercise.domain.network.model;

/**
 * Created by Raymond on 2016-04-14.
 */
public class ApiResponse {
    public int status;
    public String message;
    public ApiData[] data;
    public ApiPaging paging;

    public static class ApiData {
        public String id;
        public String caption;
        public ApiImages images;
        public String link;
        public ApiVotes votes;
        public ApiComments comments;
    }

    public static class ApiImages {
        public String small;
        public String cover;
        public String normal;
        public String large;
    }

    public static class ApiVotes {
        public int count;
    }

    public static class ApiMedia {
        public String mp4;
        public String webm;
    }

    public static class ApiComments {
        public int count;
    }

    public static class ApiPaging {
        public String next;
    }
}
