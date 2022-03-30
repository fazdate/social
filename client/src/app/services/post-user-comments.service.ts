import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HotToastService } from '@ngneat/hot-toast';
import { of } from 'rxjs';
import { urls } from 'src/environments/environment';
import { PostUserComments } from '../models/post-user-comments';

@Injectable({
  providedIn: 'root'
})
export class PostUserCommentsService {

  constructor(
    private http: HttpClient,
  ) { }

  async getPostUserCommentsObs(postId: string) {
    return of(await this.getPostUserComments(postId))
  }

  async getOwnPostUserComments(username: string) {
    return await this.getPostUserCommentsArray(username, urls.getOwnPostUserCommentsUrl)
  }

  async getOwnAndFollowedUsersPostUserComments(username: string) {
    return await this.getPostUserCommentsArray(username, urls.getOwnAndFollowedUsersPostUserCommentsUrl)
  }

  async getPostUserCommentsArray(username: string, url: string) {
    const httpParams = new HttpParams().set("username", username!)
    const result = await this.http.get<PostUserComments[]>(url, { 'params': httpParams }).toPromise()
    let posts: PostUserComments[] = []
    for (let i = 0; i < result!.length; i++) {
      posts.push({
        comments: result![i].comments,
        photoUrl: result![i].photoUrl,
        displayName: result![i].displayName,
        post: result![i].post
      })
    }
    return posts;
  }

  async getPostUserComments(postId: string) {
    const httpParams = new HttpParams().set("postId", postId)
    const result = await this.http.get<PostUserComments>(urls.getPostUserCommentUrl, { 'params': httpParams }).toPromise()
    return {
      comments: result?.comments,
      photoUrl: result?.photoUrl,
      displayName: result?.displayName,
      post: result?.post
    }
  }

}
