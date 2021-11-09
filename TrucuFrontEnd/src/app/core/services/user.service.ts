import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Account } from '../models/Account';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  user : Account;

  userSubject = new BehaviorSubject<Account>(new Account);


  constructor() {
    this.userSubject.subscribe(data => this.user = data, error => console.log(error));
  
  }

  setUser(value : Account){
    this.userSubject.next(value);
  }

  isLogged(){
    return this.user.email != null;
  }
}
