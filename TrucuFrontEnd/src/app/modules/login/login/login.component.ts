import { Component, Input, OnInit } from '@angular/core';
import { HttpService } from 'src/app/core/services/http.service';
import { UserService } from 'src/app/core/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  @Input() showModal : boolean = false;

  email : string;
  password : string;

  constructor(public http : HttpService, public userService : UserService) { }

  ngOnInit(): void {
  }

  toggleModal(){
    this.showModal = !this.showModal;
  }

  login(){
    var account = {email : this.email, password : this.password};
    this.http.Login(account).subscribe(data => {
      if(data != null){
        this.userService.setUser(data);
      }
    });
  }
}
