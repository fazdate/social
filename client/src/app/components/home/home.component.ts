import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Timestamp } from 'firebase/firestore';
import { concatMap } from 'rxjs';
import { Post } from 'src/app/models/post';
import { ImageUploadService } from 'src/app/services/image-upload.service';
import { PostsService } from 'src/app/services/posts.service';
import { UsersService } from 'src/app/services/users.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  user$ = this.usersService.currentUserProfile$;
  isCreatingPost = false
  imageFile!: File
  doesPostHaveImage = false
  username = ""

  constructor(
    private usersService: UsersService,
    private postService: PostsService,
    private imageUploadService: ImageUploadService,
  ) { }

  ngOnInit(): void {
    this.user$.subscribe(result => {
      this.username = result.username!
    })
  }

  postForm = new FormGroup({
    text: new FormControl('', Validators.required),
  })


  createPost() {
    this.postService.generatePostId().then(res => {
      let post: Post = {
        postId: res?.valueOf(),
        text: this.postForm.get('text')?.value,
        posterUsername: this.username,
        commentIds: [],
        imgSrc: "",
        timestamp: Timestamp.now().toDate(),
        usersThatLiked: []
      }
      if (this.doesPostHaveImage) {
        this.imageUploadService.uploadImage(this.imageFile, `images/posts/${res?.valueOf()}`)
          .pipe(
            concatMap(
              async (photoURL) => post.imgSrc = photoURL
            )
          )
      }
      this.postService.createPost(post)
    })
  }

  showHidePostCreater() {
    this.isCreatingPost = !this.isCreatingPost
  }

  uploadImage(event: any) {
    this.imageFile = event.target.files[0]
    this.doesPostHaveImage = true
  }

}
