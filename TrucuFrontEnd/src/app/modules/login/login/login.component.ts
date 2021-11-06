import { Component, Input, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { constants } from 'src/app/core/constants';
import { Account } from 'src/app/core/models/Account';
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

  isCreating : boolean = false;
  account : Account = new Account();
  confirmPassword : string;

  constructor(public http : HttpService, public userService : UserService, private toastr: ToastrService) { }

  ngOnInit(): void {
  }

  toggleModal(){
    this.showModal = !this.showModal;
  }

  login(){
    var account = {email : this.email, password : this.password};
    this.http.Login(account).subscribe(result => {
      
      this.userService.setUser(result);
        this.toastr.success('Conectado correctamente', 'Exito');
    },
    error => {
      this.toastr.error('Usuario incorrecto','Error');
    });
  }

  toggleRegistration(){
    this.isCreating = !this.isCreating;
  }

  registerUser(){
    this.account.rolName = constants.USERROLE;
    if(this.account.password != this.confirmPassword){
      this.toastr.warning('Las contraseñas no coinciden', 'Alerta');
      return;
    }
    if(!this.checkNotNull()){
      this.toastr.warning('Falta rellenar datos', 'Alerta');
      return;
    }
    
    this.http.Register(this.account).subscribe(data => {
      this.toggleRegistration();
    });
  }

  checkNotNull(){
    if(this.account.birthDate && this.account.ci && this.account.email && this.account.lastName && this.account.name && this.account.password && this.account.rolName){
      return true;
    }else{
      return false;
    }
  }
}
