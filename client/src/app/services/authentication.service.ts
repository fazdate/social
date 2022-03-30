import { Injectable } from '@angular/core';
import { Auth } from '@angular/fire/auth';
import { signInWithEmailAndPassword, updateProfile, UserInfo } from 'firebase/auth';
import { authState } from 'rxfire/auth';
import { concatMap, from, Observable, of } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { urls } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  currentUser$ = authState(this.auth)

  constructor(
    private auth: Auth,
    private http: HttpClient
  ) { }

  login(username: string, password: string) {
    return from(signInWithEmailAndPassword(this.auth, username, password))
  }

  signUp(formData: Object, password: string) {
    const httpParams = new HttpParams().set("password", password)
    return from(this.http.post(urls.createUserUrl, formData, { 'params': httpParams }))
  }

  updateProfile(profileData: Partial<UserInfo>): Observable<any> {
    const user = this.auth.currentUser;
    return of(user).pipe(
      concatMap(user => {
        if (!user) {
          throw new Error('Not Authenticated')
        }
        return updateProfile(user, profileData)
      })
    )
  }

  logout() {
    return from(this.auth.signOut())
  }
}
