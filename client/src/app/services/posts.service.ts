import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HotToastService } from '@ngneat/hot-toast';
import { Observable } from 'rxjs';
import { Post } from '../models/post';
import { postWithImage } from '../models/postWithImage';
import { ProfileUser } from '../models/user-profile';

interface PostArray {
  posts: Post[]
}

@Injectable({
  providedIn: 'root'
})
export class PostsService {


  serverURL = "http://localhost:8080"

  constructor(
    private http: HttpClient,
    private toast: HotToastService
    ) { }

  async createPost(post: Post) {
    this.http.post(`${this.serverURL}/createPost`, post).pipe(
      this.toast.observe(
        {
          loading: 'Uploading post...',
          success: 'Post was uploaded successfully',
          error: 'There was an error while uploading post',
        })).subscribe()
  }

  async generatePostId() {
    const result = await this.http.get<string>(`${this.serverURL}/generatePostId`).toPromise()
    let postId = result?.valueOf()
    return postId
  }

  async getOwnAndFollowedUsersPosts(username: string) {
    return await this.getPostArray(username, "getOwnAndFollowedUsersPosts")
  }


  async getFollowedUsersPosts(username: string) {
    return await this.getPostArray(username, "getFollowedUsersPosts")
  }

  async getOwnPost(username: string) {
    return await this.getPostArray(username, "getOwnPosts")
  }

  private async getPostArray(username: string, url: string) {
    const httpParams = new HttpParams().set("username", username!)
    const result = await this.http.get<Post[]>(`${this.serverURL}/${url}`, { 'params': httpParams }).toPromise()
    let posts: Post[] = []
    for (let i = 0; i < result!.length; i++) {
      posts.push({
        imgSrc: result![i].imgSrc,
        postId: result![i].postId,
        posterUsername: result![i].posterUsername,
        text: result![i].text,
        timestamp: result![i].timestamp,
        usersThatLiked: result![i].usersThatLiked
      })
    }
    return posts;
  }

  async getPost(postId: string) {
    const httpParams = new HttpParams().set("postId", postId)
    const result = await this.http.get<Post>(`${this.serverURL}/getPost`, { 'params': httpParams }).toPromise()
    return {
      imgSrc: result?.imgSrc,
      postId: result?.postId,
      posterUsername: result?.posterUsername,
      text: result?.text,
      timestamp: result?.timestamp,
      usersThatLiked: result?.usersThatLiked
    }
  }
}
