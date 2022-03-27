import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';import { TranslocoService } from '@ngneat/transloco';
;
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
    private usersService: UsersService,
    private translocoService: TranslocoService
  ) { }

  ngOnInit(): void {
    this.translocoService.setActiveLang(localStorage.getItem("language")!)
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
      window.location.reload()
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

  selectLanguage(locale: string) {
    localStorage.setItem("language", locale)
    this.translocoService.setActiveLang(locale);
  }

}
