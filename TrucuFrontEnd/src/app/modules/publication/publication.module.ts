import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreatePublicationComponent } from './create-publication/create-publication.component';
import { PublicationRoutingModule } from './publication-routing.module';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    CreatePublicationComponent
  ],
  imports: [
    CommonModule,
    PublicationRoutingModule,
    FormsModule
  ],
  exports : [
    CreatePublicationComponent
  ]
})
export class PublicationModule { }
