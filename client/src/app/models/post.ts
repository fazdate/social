export interface Post {
    imgSrc?: string;
    postId?: string;
    posterUsername?: string;
    text?: string;
    timestamp?: Date;
    usersThatLiked?: string[];
    commentIds?: string[];
}