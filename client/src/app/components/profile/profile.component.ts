import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Params } from '@angular/router';
import { UntilDestroy } from '@ngneat/until-destroy';
import { concatMap, Observable } from 'rxjs';
import { ProfileUser } from 'src/app/models/user-profile';
import { ImageUploadService } from 'src/app/services/image-upload.service';
import { UsersService } from 'src/app/services/users.service';

@UntilDestroy()
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  currentUser$ = this.usersService.currentUserProfile$
  curretUserName = ""
  user$!: Observable<ProfileUser>
  username = ""
  isOwnProfile = false
  isChangingProfile = false
  followButtonText = ""

  profileForm = new FormGroup({
    displayName: new FormControl(''),
  });

  constructor(
    private imageUploadService: ImageUploadService,
    private usersService: UsersService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => {

      this.usersService.getUserObservable(params['username']).then(result => {

        this.user$ = result

        result.subscribe(res => {

          this.username = res.username!

          this.profileForm.setValue({
            displayName: res.displayName
          })

          this.currentUser$.subscribe(userRes => {
            this.curretUserName = userRes.username!
            if (res.username === userRes.username) {
              this.isOwnProfile = true
            }
            for (let name of userRes.followedUsers!) {
              if (name == res.username!) {
                this.followButtonText = "Unfollow"
              } else {
                this.followButtonText = "Follow"
              }
            }
          })
        })
      })

    })

  }

  uploadImage(event: any, user: ProfileUser) {
    this.imageUploadService.uploadImage(event.target.files[0], `images/profile/${user.username}`)
      .pipe(
        concatMap(
          async (photoURL) => this.usersService.updateUsersPhoto(user.username as string, photoURL)
        )
      ).subscribe()
  }

  updateDisplayName(user: ProfileUser) {
    const newName = this.profileForm.get('displayName')?.value;
    this.usersService.updateDisplayName(user.username as string, newName as string)
    this.setisChangingProfile()
  }

  follow() {
    this.usersService.followOrUnfollowUser(this.username!, this.curretUserName)
  }

  setisChangingProfile() {
    this.isChangingProfile = !this.isChangingProfile
  }

}


