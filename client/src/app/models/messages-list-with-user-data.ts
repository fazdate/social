import { MessagesList } from "./messages-list";

export interface MessagesListWithUserData {
    messagesList: MessagesList
    user1DisplayName: string
    user1PhotoUrl: string
    user2DisplayName: string
    user2PhotoUrl: string
}