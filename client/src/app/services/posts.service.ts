import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HotToastService } from '@ngneat/hot-toast';
import { Post } from '../models/post';
import '@angular/common/locales/hu';
import { urls } from 'src/environments/environment';
import { TranslocoService } from '@ngneat/transloco';

@Injectable({
  providedIn: 'root'
})
export class PostsService {

  constructor(
    private http: HttpClient,
    private toast: HotToastService,
    private transloco: TranslocoService
  ) { }


  async createPost(post: Post) {
    this.http.post(urls.createPostUrl, post).pipe(
      this.toast.observe(
        {
          loading: this.transloco.translate('posts-service-toast-loading'),
          success: this.transloco.translate('posts-service-toast-success'),
          error: this.transloco.translate('posts-service-toast-error'),
        })).subscribe()
    window.location.reload();
  }

  async generatePostId() {
    const result = await this.http.get<string>(urls.generatePostIdUrl).toPromise()
    let postId = result?.valueOf()
    return postId
  }

  async likeOrUnlikePost(postId: string, username: string) {
    let httpParams = new HttpParams().set("postId", postId).append("username", username)
    this.http.put(urls.likeOrUnlikePostUrl, null, { 'params': httpParams }).toPromise()
    window.location.reload();
  }

}
