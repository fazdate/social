import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { PostUserCommentsService } from 'src/app/services/post-user-comments.service';
import { PostUserComments } from 'src/app/models/postAndUserAndComments';
import { ProfileUser } from 'src/app/models/user-profile';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {

  @Input()
  type!: string

  @Input()
  user!: Observable<ProfileUser> | null

  constructor(
    private postUserCommentsService: PostUserCommentsService
  ) {}

  ngOnInit(): void {
    
  }

  posts: PostUserComments[] = [];
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['user']) {
      this.user!.subscribe(result => {
        switch (this.type) {
          case "ownAndFollowedUsersPosts":
            this.postUserCommentsService.getOwnAndFollowedUsersPosts(result.username!).then(result => this.posts = result)
            break
          case "ownPosts":
            this.postUserCommentsService.getOwnPosts(result.username!).then(result => this.posts = result)
            break;
        }
      })
    }
  }

  like() {

  }

  comment() {

  }

}
