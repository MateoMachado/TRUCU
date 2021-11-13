import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeScreenComponent } from './home-screen/home-screen.component';
import { HomeRoutingModule } from './home-routing.module';
import { PublicationModule } from '../publication/publication.module';



@NgModule({
  declarations: [
    HomeScreenComponent
  ],
  imports: [
    CommonModule,
    HomeRoutingModule,
    PublicationModule
  ],
  exports: [
    HomeScreenComponent
  ]
})
export class HomeModule { }
