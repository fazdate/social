import { Timestamp } from "firebase/firestore";

export interface Comment {
    commentId?: string;
    commenterUsername?: string;
    postId?: string;
    text?: string;
    timestamp?: Timestamp;
}