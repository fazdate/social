import { Injectable } from '@angular/core';
import { Post } from '../models/post';
import { PostUserComments } from '../models/postAndUserAndComments';
import { CommentService } from './comment.service';
import { PostsService } from './posts.service';
import { UsersService } from './users.service';

@Injectable({
  providedIn: 'root'
})
export class PostUserCommentsService {

  constructor(
    private postService: PostsService,
    private usersService: UsersService,
    private commentService: CommentService
  ) { }

  async getOwnPosts(username: string) {
    let ownPosts = await this.postService.getOwnPost(username)
    return await this.getPostUserCommentsArray(ownPosts)
  }

  async getOwnAndFollowedUsersPosts(username: string) {
    let ownAndFollowedUsersPosts = await this.postService.getOwnAndFollowedUsersPosts(username)
    return await this.getPostUserCommentsArray(ownAndFollowedUsersPosts)
  }

  async getPostUserCommentsArray(posts: Post[]) {
    let PostUserComments: PostUserComments[] = []
    for (let i = 0; i < posts.length; i++) {
      let userId = posts[i].posterUsername as string
      let user = await this.usersService.getUser(userId)
      let comments = await this.commentService.getEveryCommentOfPost(posts[i].postId as string)
      PostUserComments.push( {
        post: posts![i],
        photoUrl: user.photoURL,
        displayName: user.displayName,
        comments: comments
      }) 
    }
    return PostUserComments
  }
}
