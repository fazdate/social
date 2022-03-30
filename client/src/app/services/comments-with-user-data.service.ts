import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { urls } from 'src/environments/environment';
import { CommentWithUserData } from '../models/comment-with-user-data';

@Injectable({
  providedIn: 'root'
})
export class CommentsWithUserDataService {

  constructor(
    private http: HttpClient
  ) { }

  async getCommentWithUserData(commentId: string) {
    const httpParams = new HttpParams().set("commentId", commentId)
    const result = await this.http.get<CommentWithUserData>(urls.getCommentWithUserDataUrl, { 'params': httpParams }).toPromise()
    return {
      comment: result?.comment,
      photoUrl: result?.photoUrl,
      displayName: result?.displayName,
    }
  }
}
