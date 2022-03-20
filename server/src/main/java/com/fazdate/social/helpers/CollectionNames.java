package com.fazdate.social.helpers;

public class CollectionNames {
    private final static String users = "users";
    private final static String posts = "posts";
    private final static String comments = "comments";

    public static String getCollectionName(Names name) {
        return switch (name) {
            case USERS -> users;
            case POSTS -> posts;
            case COMMENTS -> comments;
        };
    }
}
