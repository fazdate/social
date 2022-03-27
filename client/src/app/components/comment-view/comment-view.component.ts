import { DatePipe } from '@angular/common';
import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Timestamp } from 'firebase/firestore';
import { Observable } from 'rxjs';
import { Comment } from 'src/app/models/comment';
import { CommentWithUserData } from 'src/app/models/comment-with-user-data';
import { CommentService } from 'src/app/services/comment.service';
import { CommentsWithUserDataService } from 'src/app/services/comments-with-user-data.service';
import { UsersService } from 'src/app/services/users.service';

@Component({
  selector: 'app-comment-view',
  templateUrl: './comment-view.component.html',
  styleUrls: ['./comment-view.component.css']
})
export class CommentViewComponent implements OnInit {

  @Input()
  comments!: Observable<Comment[]>

  user$ = this.usersService.currentUserProfile$;
  username: string = ""
  postId: string = ""
  commentsWithUserData: CommentWithUserData[] = []
  commentForm = new FormGroup({
    text: new FormControl('', Validators.required),
  })

  constructor(
    private commentsWithUserDataService: CommentsWithUserDataService,
    private commentService: CommentService,
    private usersService: UsersService,
    private datePipe: DatePipe,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: Params) => {
      this.postId = params['postId']
      this.user$.subscribe(result => {
        this.username = result.username!
      })
    })
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['comments']) {
      this.comments.subscribe(result => {
        for (let comment of result) {
          this.commentsWithUserDataService.getCommentWithUserData(comment.commentId!).then(res => {
            this.commentsWithUserData.push(res as CommentWithUserData)
          })
        }
      })
    }
  }

  sendComment() {
    if (!this.commentForm.valid) {
      return;
    }
       
    this.commentService.generateCommentId().then(res => {
      let comment: Comment = {
        commentId: res?.valueOf(),
        postId: this.postId,
        commenterUsername: this.username,
        text: this.commentForm.get('text')?.value,
        timestamp: Timestamp.now().toDate()
      }
      this.commentService.addComment(comment)
    })
  }

  getDate(date: Date) {
    return this.datePipe.transform(date, 'MMMM dd, HH:ss');
  }

  redirectToPosterProfile(commenterUsername: string) {  
    let link = "/profile/" + commenterUsername
    this.router.navigate([link])
  }
}
