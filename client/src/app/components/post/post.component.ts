import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { PostUserCommentsService } from 'src/app/services/post-user-comments.service';
import { PostUserComments } from 'src/app/models/post-user-comments';
import { ProfileUser } from 'src/app/models/user-profile';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { PostsService } from 'src/app/services/posts.service';

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

  @Input()
  post!: Observable<PostUserComments>

  username = ""
  posts: PostUserComments[] = [];

  constructor(
    private postUserCommentsService: PostUserCommentsService,
    private router: Router,
    private postService: PostsService
  ) { }

  ngOnInit(): void {
    if (this.type === "single") {
      this.user?.subscribe(result => {
        this.username = result.username!
      })
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['user']) {
      this.user!.subscribe(result => {
        this.username = result.username!
        switch (this.type) {
          case "ownAndFollowedUsersPosts":
            this.postUserCommentsService.getOwnAndFollowedUsersPostUserComments(result.username!).then(result => this.posts = result)
            break
          case "ownPosts":
            this.postUserCommentsService.getOwnPostUserComments(result.username!).then(result => this.posts = result)
            break;
        }
      })
    }

    if (changes['post']) {
      this.post.subscribe(result => {
        this.posts[0] = result
      })
    }
  }

  likeOrUnlike(postId: string) {
    this.postService.likeOrUnlikePost(postId!, this.username)
  }

  isLiked(post: PostUserComments): boolean {
    if (post.post?.usersThatLiked?.includes(this.username)) {
      return true
    }
    return false
  }

  redirectToPostPage(postId: string) {
    let link = "/post/" + postId
    this.router.navigate([link])
  }

  redirectToPosterProfile(posterUsername: string) {
    let link = "/profile/" + posterUsername
    this.router.navigate([link])
  }

}
