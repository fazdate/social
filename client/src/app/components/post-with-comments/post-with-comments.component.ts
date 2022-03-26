import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Observable, of } from 'rxjs';
import { Comment } from 'src/app/models/comment';
import { PostUserComments } from 'src/app/models/post-user-comments';
import { PostUserCommentsService } from 'src/app/services/post-user-comments.service';
import { UsersService } from 'src/app/services/users.service';

@Component({
  selector: 'app-post-with-comments',
  templateUrl: './post-with-comments.component.html',
  styleUrls: ['./post-with-comments.component.css']
})
export class PostWithCommentsComponent implements OnInit {

  user$ = this.usersService.currentUserProfile$;
  post$: Observable<PostUserComments> = of({})
  comments: Observable<Comment[]> = of([])

  constructor(
    private usersService: UsersService,
    private route: ActivatedRoute,
    private postUserCommentsService: PostUserCommentsService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => {
      this.postUserCommentsService.getPostUserCommentsObs(params['postId']).then(result => {
        this.post$ = result
        result.subscribe(res => {
          this.comments = of(res.comments!)
        })
      })
    })
  }

}
