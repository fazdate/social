import { Comment } from "./comment";
import { Post } from "./post";

export interface PostUserComments {
    post?: Post;
    photoUrl?: string;
    displayName?: string;
    comments?: Comment[];
}