import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HotToastService } from '@ngneat/hot-toast';
import { Comment } from '../models/comment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  serverURL = "http://localhost:8080"

  constructor(
    private http: HttpClient,
    private toast: HotToastService
  ) { }

  async addComment(comment: Comment) {
    this.http.post('http://localhost:8080/addComment', comment).pipe(
      this.toast.observe(
        {
          loading: 'Uploading comment...',
          success: 'Comment uploaded successfully ',
          error: 'There was an error while uploading comment',
        })).subscribe()
  }

  async getComment(commentId: string) {
    const httpParams = new HttpParams().set("commentId", commentId)
    const result = await this.http.get<Comment>(`${this.serverURL}/getComment`, { 'params': httpParams }).toPromise()
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
    await this.http.put(`${this.serverURL}/deleteComment`, { 'params': httpParams }).toPromise()
  }

  async getEveryCommentOfPost(postId: string) {
    const httpParams = new HttpParams().set("postId", postId)
    const result = await this.http.get<Comment[]>(`${this.serverURL}/getEveryCommentOfPost`, { 'params': httpParams }).toPromise()
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
