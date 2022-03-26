import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessagesList } from 'src/app/models/messages-list';
import { MessagesListWithUserData } from 'src/app/models/messages-list-with-user-data';
import { MessagesListWithUserDataService } from 'src/app/services/messages-list-with-user-data.service';
import { UsersService } from 'src/app/services/users.service';

@Component({
  selector: 'app-messages-list',
  templateUrl: './messages-list.component.html',
  styleUrls: ['./messages-list.component.css']
})
export class MessagesListComponent implements OnInit {

  currentUser$ = this.usersService.currentUserProfile$
  messagesList: MessagesListWithUserData[] = []
  displayName = ""

  constructor(
    private MessagesListWithUserDataService: MessagesListWithUserDataService,
    private usersService: UsersService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.currentUser$.subscribe(result => {
      this.displayName = result.displayName!
      this.MessagesListWithUserDataService.getUsersEveryMessagesListWithUserDatarComments(result.username!).then(res => {
        this.messagesList = res
      })
    })
  }

  getPhotoURL(messages: MessagesListWithUserData) {
    if (messages.user1DisplayName === this.displayName) {

      return messages.user2PhotoUrl
    }
    return messages.user1PhotoUrl
  }

  getDisplayName(messages: MessagesListWithUserData) {
    if (messages.user1DisplayName == this.displayName) {
      return messages.user2DisplayName
    }
    return messages.user1DisplayName
  }

  redirectToMessages(messages: MessagesListWithUserData) {
    this.router.navigateByUrl('/messages/' + messages.messagesList.messagesListId);
  }

}
