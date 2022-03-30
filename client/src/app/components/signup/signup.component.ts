import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, Validators, ValidatorFn } from '@angular/forms';
import { Router } from '@angular/router';
import { HotToastService } from '@ngneat/hot-toast';
import { TranslocoService } from '@ngneat/transloco';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  checkPasswords: ValidatorFn = (group: AbstractControl): ValidationErrors | null => {
    let pass = group.get('password')?.value
    let confirmPass = group.get('confirmPassword')?.value
    return pass === confirmPass ? null : { notSame: true }
  }

  signUpForm = new FormGroup({
    username: new FormControl('', Validators.required),
    displayName: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
    birthdate: new FormControl('', Validators.required),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
    confirmPassword: new FormControl('', [Validators.required, Validators.minLength(6)])
  }, { validators: this.checkPasswords })

  constructor(
    private authService: AuthenticationService,
    private toast: HotToastService,
    private router: Router,
    private transloco: TranslocoService
  ) { }

  ngOnInit(): void {
  }


  get username() {
    return this.signUpForm.get('username')
  }

  get displayName() {
    return this.signUpForm.get('displayName')
  }

  get email() {
    return this.signUpForm.get('email')
  }

  get birthdate() {
    return this.signUpForm.get('birthdate')
  }

  get password() {
    return this.signUpForm.get('password')
  }

  get confirmPassword() {
    return this.signUpForm.get('confirmPasword')
  }

  get formdata() {
    return {
      'username': this.username?.value,
      'displayName': this.displayName?.value,
      'email': this.email?.value,
      'birthdate': this.birthdate?.value
    }
  }

  submit() {
    if (!this.signUpForm.valid) {
      return;
    }

    this.authService
      .signUp(this.formdata, this.password?.value)
      .pipe(
        this.toast.observe({
          success: this.transloco.translate('sign-in-toast-success'),
          loading: this.transloco.translate('sign-in-toast-loading'),
          error: ({ message }) => `${message}`,
        })
      )
      .subscribe(() => {
        this.router.navigate(['/login']);
      });
  }

}

