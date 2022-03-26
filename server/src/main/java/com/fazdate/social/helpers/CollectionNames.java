package com.fazdate.social.helpers;

public class CollectionNames {
    private final static String users = "users";
    private final static String posts = "posts";
    private final static String comments = "comments";
    private final static String messages = "messages";
    private final static String messagesList = "messagesList";

    /**
     * Returns the string value of the given enum
     */
    public static String getCollectionName(Names name) {
        return switch (name) {
            case USERS -> users;
            case POSTS -> posts;
            case COMMENTS -> comments;
            case MESSAGES -> messages;
            case MESSAGESLIST -> messagesList;
        };
    }
}
