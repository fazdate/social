import { Comment } from "./comment";

export interface CommentWithUserData {
    comment: Comment,
    displayName: string,
    photoUrl: string    
}