import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HotToastService } from '@ngneat/hot-toast';
import { urls } from 'src/environments/environment';
import { Message } from '../models/message';
import { MessagesList } from '../models/messages-list';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(
    private http: HttpClient,
    private toast: HotToastService
  ) { }

  async getMessage(messageId: string) {
    const httpParams = new HttpParams().set("messageId", messageId)
    const result = await this.http.get<Message>(urls.getMessageUrl, { 'params': httpParams }).toPromise()
    return {
      messageId: result?.messageId,
      senderUsername: result?.senderUsername,
      receiverUsername: result?.receiverUsername,
      messageText: result?.messageText,
      messagesListId: result?.messagesListId,
      timestamp: result?.timestamp
    }
  }

  async getMessagesList(messagesListId: string) {
    const httpParams = new HttpParams().set("messagesListId", messagesListId)
    const result = await this.http.get<MessagesList>(urls.getMessagesListUrl, { 'params': httpParams }).toPromise()
    return {
      messagesListId: result?.messagesListId!,
      messageIds: result?.messageIds!,
      username1: result?.username1!,
      username2: result?.username2!
    }
  }

  async getEveryMessageFromMessagesList(messagesListId: string) {
    const httpParams = new HttpParams().set("messagesListId", messagesListId!)
    const result = await this.http.get<Message[]>(urls.getEveryMessageFromMessagesListUrl, { 'params': httpParams }).toPromise()
    let messages: Message[] = []
    for (let i = 0; i < result!.length; i++) {
      messages.push({
        messageId: result![i].messageId,
        messageText: result![i].messageText,
        messagesListId: result![i].messagesListId,
        receiverUsername: result![i].receiverUsername,
        senderUsername: result![i].senderUsername,
        timestamp: result![i].timestamp
      })
    }
    return messages;
  }

  async sendMessage(message: Message) {
    console.log("Sending");
    this.http.post(urls.sendMessageUrl, message).pipe(
      this.toast.observe(
        {
          loading: 'Sendig message...',
          success: 'Message sent',
          error: 'There was an error while sending message',
        })).subscribe()
    window.location.reload();
  }

  async generateMessageId() {
    const result = await this.http.get<string>(urls.generateMessageIdUrl).toPromise()
    let postId = result?.valueOf()
    return postId
  }
}
