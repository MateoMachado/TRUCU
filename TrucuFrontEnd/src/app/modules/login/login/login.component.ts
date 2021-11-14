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
      if(result.message){
        this.toastr.warning(result.message, 'Advertencia');
      }else{
        this.userService.setUser(result);
        this.toastr.success('Conectado correctamente', 'Exito');
      }
    },
    error => {
      console.log(error);
      this.toastr.error('Usuario incorrecto','Error');
    });
  }

  toggleRegistration(){
    this.isCreating = !this.isCreating;
  }

  registerUser(){
    this.account.rolName = constants.USERROLE;
    if(!this.checkNotNull()){
      this.toastr.warning('Debe rellenar todos los campos', 'Alerta');
      return;
    }
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
      this.toastr.success('Registrado correctamente','Exito');
      this.showModal = !this.showModal;
    },
    error => {
      console.log(error);
      if(error.status == 400){
        this.toastr.error('Email invalido o ya existente','Error');
      }else{
        this.toastr.error('Ha ocurrido un error inesperado','Error');
      }
    });
  }

  checkNotNull(){
    if(this.account.birthDate && this.account.email && this.account.lastName && this.account.name && this.account.password && this.account.rolName){
      return true;
    }else{
      return false;
    }
  }
}
