import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { doc, Firestore } from '@angular/fire/firestore';
import { HotToastService } from '@ngneat/hot-toast';
import { docData } from 'rxfire/firestore';
import { Observable, of, switchMap } from 'rxjs';
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
    private toast: HotToastService
  ) { }

  async updateUser(user: ProfileUser) {
    this.http.post('http://localhost:8080/updateUser', user).pipe(
      this.toast.observe(
        {
          loading: 'Updating profile...',
          success: 'Profile updated successfully',
          error: 'There was an error while updating profile',
        })).subscribe()
  }

  async updateDisplayName(username: string, newDisplayName: string) {
    let user = await this.getUser(username)
    user.displayName = newDisplayName
    await this.updateUser(user)
  }

  async updateUsersPhoto(username: string, photoURL: string) {
    let user = await this.getUser(username)
    user.photoURL = photoURL
    await this.updateUser(user)
  }

  async getUser(username: string) {
    const httpParams = new HttpParams().set("username", username)
    const result = await this.http.get<ProfileUser>('http://localhost:8080/getUser', { 'params': httpParams }).toPromise()
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
    return this.http.get<ProfileUser>('http://localhost:8080/getUser', { 'params': httpParams })
  }

  async followOrUnfollowUser(username: string, anotherUsername: string) {
    let httpParams = new HttpParams().set("username", username).append("anotherUsername", anotherUsername)
    this.http.put('http://localhost:8080/followOrUnfollowUser', null, { 'params': httpParams }).toPromise()
  }

  async getEveryUsername() {
    const result = await this.http.get<string[]>('http://localhost:8080/getEveryUsername').toPromise()
    let usernames: string[] = []
    for (let i = 0; i < result!.length; i++) {
      usernames.push(result![i])
    }
    return usernames
  }
}

