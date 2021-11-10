import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreatePublicationComponent } from './create-publication/create-publication.component';
import { PublicationRoutingModule } from './publication-routing.module';
import { FormsModule } from '@angular/forms';
import { ViewPublicationComponent } from './view-publication/view-publication.component';



@NgModule({
  declarations: [
    CreatePublicationComponent,
    ViewPublicationComponent
  ],
  imports: [
    CommonModule,
    PublicationRoutingModule,
    FormsModule
  ],
  exports : [
    CreatePublicationComponent,
    ViewPublicationComponent
  ]
})
export class PublicationModule { }
