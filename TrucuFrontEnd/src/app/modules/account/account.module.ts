import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ViewAccountComponent } from './view-account/view-account.component';



@NgModule({
  declarations: [
    ViewAccountComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    ViewAccountComponent
  ]
})
export class AccountModule { }
