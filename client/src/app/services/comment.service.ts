import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HotToastService } from '@ngneat/hot-toast';
import { TranslocoService } from '@ngneat/transloco';
import { urls } from 'src/environments/environment';
import { Comment } from '../models/comment';


@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(
    private http: HttpClient,
    private toast: HotToastService,
    private transloco: TranslocoService
  ) { }

  async addComment(comment: Comment) {
    this.http.post(urls.addCommentUrl, comment).pipe(
      this.toast.observe(
        {
          loading: this.transloco.translate('comment-service-toast-loading'),
          success: this.transloco.translate('comment-service-toast-success'),
          error: this.transloco.translate('comment-service-toast-error'),
        })).subscribe()
        window.location.reload();
  }

  async generateCommentId() {
    const result = await this.http.get<string>(urls.generateCommentIdUrl).toPromise()
    let postId = result?.valueOf()
    return postId
  }

  async getComment(commentId: string) {
    const httpParams = new HttpParams().set("commentId", commentId)
    const result = await this.http.get<Comment>(urls.getCommentUrl, { 'params': httpParams }).toPromise()
    return {
      commentId: result?.commentId,
      commenterUsername: result?.commenterUsername,
      postId: result?.postId,
      text: result?.text,
      timestamp: result?.timestamp
    }
  }

  async deleteComment(commentId: string) {
    const httpParams = new HttpParams().set("commentId", commentId)
    await this.http.put(urls.deleteCommentUrl, { 'params': httpParams }).toPromise()
  }

  async getEveryCommentOfPost(postId: string) {
    const httpParams = new HttpParams().set("postId", postId)
    const result = await this.http.get<Comment[]>(urls.getEveryCommentOfPostUrl, { 'params': httpParams }).toPromise()
    let comments: Comment[] = []
    for (let i = 0; i < result!.length; i++) {
      comments.push({
        commentId: result![i].commentId,
        commenterUsername: result![i].commenterUsername,
        postId: result![i].postId,
        text: result![i].text,
        timestamp: result![i].timestamp
      })
    }
    return comments;
  }

}
