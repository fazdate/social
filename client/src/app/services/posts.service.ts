import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HotToastService } from '@ngneat/hot-toast';
import { Post } from '../models/post';
import '@angular/common/locales/hu';
import { urls } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PostsService {

  constructor(
    private http: HttpClient,
    private toast: HotToastService,
  ) { }


  async createPost(post: Post) {
    this.http.post(urls.createPostUrl, post).pipe(
      this.toast.observe(
        {
          loading: 'Uploading post...',
          success: 'Post was uploaded successfully',
          error: 'There was an error while uploading post',
        })).subscribe()
    window.location.reload();
  }

  async generatePostId() {
    const result = await this.http.get<string>(urls.generatePostId).toPromise()
    let postId = result?.valueOf()
    return postId
  }

  async likeOrUnlikePost(postId: string, username: string) {
    let httpParams = new HttpParams().set("postId", postId).append("username", username)
    this.http.put(urls.likeOrUnlikePostUrl, null, { 'params': httpParams }).toPromise()
    window.location.reload();
  }

}
