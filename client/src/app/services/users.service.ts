import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { doc, Firestore } from '@angular/fire/firestore';
import { HotToastService } from '@ngneat/hot-toast';
import { TranslocoService } from '@ngneat/transloco';
import { docData } from 'rxfire/firestore';
import { Observable, of, switchMap } from 'rxjs';
import { urls } from 'src/environments/environment';
import { ProfileUser } from '../models/user-profile';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  get currentUserProfile$(): Observable<ProfileUser> {
    return this.authenticationService.currentUser$.pipe(
      switchMap(user => {
        if (!user?.uid) {
          return of(null as any)
        }

        const ref = doc(this.firestore, 'users', user?.uid)
        return docData(ref) as Observable<ProfileUser>
      })
    )
  }

  constructor(
    private firestore: Firestore,
    private authenticationService: AuthenticationService,
    private http: HttpClient,
    private toast: HotToastService,
    private transloco: TranslocoService
  ) { }

  async updateUser(user: ProfileUser) {
    this.http.post(urls.updateUserUrl, user).pipe(
      this.toast.observe(
        {
          loading: this.transloco.translate('users-service-toast-loading'),
          success: this.transloco.translate('users-service-toast-success'),
          error: this.transloco.translate('users-service-toast-error'),
        })).subscribe()
  }

  async updateDisplayName(username: string, newDisplayName: string) {
    let user = await this.getUser(username)
    user.displayName = newDisplayName
    await this.updateUser(user)
  }

  async changeEmail(username: string, newEmail: string) {
    let httpParams = new HttpParams().set("username", username).append("newEmail", newEmail)
    this.http.put(urls.changeEmailUrl, null, { 'params': httpParams }).toPromise()
  }

  async changePassword(username: string, newPassword: string) {
    let httpParams = new HttpParams().set("username", username).append("newPassword", newPassword)
    this.http.put(urls.changeEmailUrl, null, { 'params': httpParams }).toPromise()
  }


  async updateUsersPhoto(username: string, photoURL: string) {
    let user = await this.getUser(username)
    user.photoURL = photoURL
    await this.updateUser(user)
  }

  async getUser(username: string) {
    const httpParams = new HttpParams().set("username", username)
    const result = await this.http.get<ProfileUser>(urls.getUserUrl, { 'params': httpParams }).toPromise()
    return {
      username: result?.username,
      email: result?.email,
      displayName: result?.displayName,
      birthdate: result?.birthdate,
      photoURL: result?.photoURL,
      posts: result?.posts,
      followedUsers: result?.followedUsers,
      followers: result?.followers
    }
  }

  async getUserObservable(username: string) {
    const httpParams = new HttpParams().set("username", username)
    return this.http.get<ProfileUser>(urls.getUserUrl, { 'params': httpParams })
  }

  async followOrUnfollowUser(username: string, anotherUsername: string) {
    let httpParams = new HttpParams().set("username", username).append("anotherUsername", anotherUsername)
    this.http.put(urls.followOrUnfollowUserUrl, null, { 'params': httpParams }).toPromise()
    window.location.reload()
  }

  async getEveryUsername() {
    const result = await this.http.get<string[]>(urls.getEveryUsernameUrl).toPromise()
    let usernames: string[] = []
    for (let i = 0; i < result!.length; i++) {
      usernames.push(result![i])
    }
    return usernames
  }

  async getDisplayNameFromUsername(username: string) {
    let httpParams = new HttpParams().set("username", username)
    const result = await this.http.get<string>(urls.getDisplayNameFromUsernameUrl, { 'params': httpParams }).toPromise()
    let displayName = result?.valueOf()
    return displayName
  }
}

