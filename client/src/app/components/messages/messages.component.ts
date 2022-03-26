import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { throws } from 'assert';
import { Timestamp } from 'firebase/firestore';
import { Message } from 'src/app/models/message';
import { MessagesList } from 'src/app/models/messages-list';
import { MessagesListWithUserData } from 'src/app/models/messages-list-with-user-data';
import { ProfileUser } from 'src/app/models/user-profile';
import { MessageService } from 'src/app/services/message.service';
import { MessagesListWithUserDataService } from 'src/app/services/messages-list-with-user-data.service';
import { UsersService } from 'src/app/services/users.service';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit {

  currentUser$ = this.usersService.currentUserProfile$
  ownUsername = ""
  ownPhotoUrl = ""
  otherUserDisplayName = ""
  otherUsername = ""
  otherUserPhotoUrl = ""
  messagesListId = ""
  messagesListWithUserData!: MessagesListWithUserData
  messages: Message[] = []
  isWritingMessage = true // false
  messageForm = new FormGroup({
    text: new FormControl('', Validators.required),
  })

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private messagesListWithUserDataService: MessagesListWithUserDataService,
    private messageService: MessageService,
    private usersService: UsersService,
    private datePipe: DatePipe,
  ) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: Params) => {
      this.messagesListId = params['messagesListId']
      this.currentUser$.subscribe(result => {
        this.ownUsername = result.username!
        this.ownPhotoUrl = result.photoURL!
      })
      this.messagesListWithUserDataService.getMessageListWIthUserData(params['messagesListId'])
        .then(result => {
          this.messagesListWithUserData = result
        })
        this.messageService.getEveryMessageFromMessagesList(params['messagesListId'])
        .then(result => {
          this.messages = result
          this.otherUsername = this.getOtherUsername()!
          this.usersService.getUser(this.otherUsername).then(res => {
            this.otherUserPhotoUrl = res.photoURL!,
            this.otherUserDisplayName = res.displayName!
          })
        })
    })
  }

  isOwnMessage(message: Message) {
    if (message.senderUsername === this.ownUsername) {
      return true;
    }
    return false;
  }

  getOtherUsername() {
    if (this.messages[0].receiverUsername === this.ownUsername) {
      return this.messages[0].senderUsername
    }
    return this.messages[0].receiverUsername
  }

  getDate(date: Date) {
    return this.datePipe.transform(date, 'EEEE, HH:ss');
  }

  getOtherUserPhotoUrl() {
    if (this.messagesListWithUserData.user1PhotoUrl === this.ownPhotoUrl) {
      return this.messagesListWithUserData.user2DisplayName
    }
    return this.messagesListWithUserData.user1DisplayName
  }

  redirectToMessagesList() {
    this.router.navigateByUrl('/messagesList')
  }

  showMessageWriter() {
    this.isWritingMessage = !this.isWritingMessage
  }

  sendMessage () {
    if (!this.messageForm.valid) {
      return;
    }

    this.messageService.generateMessageId()
    .then(result => {
      let message: Message = {
        messageId: result?.valueOf(),
        messagesListId: this.messagesListId,
        messageText: this.messageForm.get('text')?.value,
        senderUsername: this.ownUsername,
        receiverUsername: this.otherUsername,
        timestamp: Timestamp.now().toDate()
      }
      console.log(message);
      this.messageService.sendMessage(message)
    })
  }

}
