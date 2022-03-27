import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { canActivate, redirectLoggedInTo, redirectUnauthorizedTo } from '@angular/fire/auth-guard';
import { ProfileComponent } from './components/profile/profile.component';
import { PostWithCommentsComponent } from './components/post-with-comments/post-with-comments.component';
import { MessagesListComponent } from './components/messages-list/messages-list.component';
import { MessagesComponent } from './components/messages/messages.component';

const redirectToLogin = () => redirectUnauthorizedTo(['login']);
const redirectToHome = () => redirectLoggedInTo(['home']);

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    ...canActivate(redirectToLogin),
  },
  {
    path: 'login',
    component: LoginComponent,
    ...canActivate(redirectToHome)
  },
  {
    path: 'signup',
    component: SignupComponent,
    ...canActivate(redirectToHome),
  },
  {
    path: 'profile/:username',
    component: ProfileComponent,
    ...canActivate(redirectToLogin)
  },
  {
    path: 'post/:postId',
    component: PostWithCommentsComponent,
    ...canActivate(redirectToLogin)
  },
  {
    path: 'messagesList',
    component: MessagesListComponent,
    ...canActivate(redirectToLogin)
  },
  {
    path: 'messages/:messagesListId',
    component: MessagesComponent,
    ...canActivate(redirectToLogin)
  },
];

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
