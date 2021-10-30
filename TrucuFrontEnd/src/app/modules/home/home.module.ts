import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeScreenComponent } from './home-screen/home-screen.component';



@NgModule({
  declarations: [
    HomeScreenComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    HomeScreenComponent
  ]
})
export class HomeModule { }
