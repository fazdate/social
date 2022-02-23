package com.fazdate.social.helpers;

public class CollectionNames {
    private final static String users = "users";
    private final static String posts = "posts";
    private final static String message = "message";
    private final static String messages = "messages";

    public static String getCollectionName(Names name) {
        return switch (name) {
            case USERS -> users;
            case POSTS -> posts;
            case MESSAGE -> message;
            case MESSAGES -> messages;
        };
    }
}
