import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { urls } from 'src/environments/environment';
import { MessagesListWithUserData } from '../models/messages-list-with-user-data';

@Injectable({
  providedIn: 'root'
})
export class MessagesListWithUserDataService {

  constructor(
    private http: HttpClient
  ) { }
  
  async getMessageListWIthUserData(messagesListId: string) {
    const httpParams = new HttpParams().set("messagesListId", messagesListId)
    const result = await this.http.get<MessagesListWithUserData>(urls.getMessagesListWIthUserDataUrl, { 'params': httpParams }).toPromise()
    return {
      messagesList: result?.messagesList!,
      user1DisplayName: result?.user1DisplayName!,
      user1PhotoUrl: result?.user1PhotoUrl!,
      user2DisplayName: result?.user2DisplayName!,
      user2PhotoUrl: result?.user2PhotoUrl!
    }
  }

  async getUsersEveryMessagesListWithUserDatarComments(username: string) {
    const httpParams = new HttpParams().set("username", username)
    const result = await this.http.get<MessagesListWithUserData[]>(urls.getUsersEveryMessagesListWithUserDatUrl, { 'params': httpParams }).toPromise()
    let messagesListWithUserData: MessagesListWithUserData[] = []
    for (let i = 0; i <result!.length; i++) {
      messagesListWithUserData.push ({
        messagesList: result![i].messagesList,
        user1DisplayName: result![i].user1DisplayName,
        user1PhotoUrl: result![i].user1PhotoUrl,
        user2DisplayName: result![i].user2DisplayName,
        user2PhotoUrl: result![i].user2PhotoUrl
      })
    }
    return messagesListWithUserData
  }

}
