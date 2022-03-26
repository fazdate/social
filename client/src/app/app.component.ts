import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';;
import { AuthenticationService } from './services/authentication.service';
import { UsersService } from './services/users.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  search: FormControl = new FormControl('')
  user$ = this.usersService.currentUserProfile$
  options: string[] = []
  profileLink!: string

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private usersService: UsersService
  ) { }

  ngOnInit(): void {
    this.usersService.getEveryUsername().then(result =>{
      this.options = result
    })
    this.user$.subscribe(result => {
      this.profileLink = "/profile/" + result.username
    })

  }

  logout() {
    this.authService.logout().subscribe(() => {
      this.router.navigate([''])
    })
  }

  redirectToUser() {
    let link = "/profile/" + this.search.value
    this.search.reset()
    this.router.navigate([link])
  }

  redirectToMessagesList() {
    this.router.navigate(["/messagesList"])
  }


}
