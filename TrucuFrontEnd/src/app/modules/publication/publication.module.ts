import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreatePublicationComponent } from './create-publication/create-publication.component';
import { PublicationRoutingModule } from './publication-routing.module';
import { FormsModule } from '@angular/forms';
import { ViewPublicationComponent } from './view-publication/view-publication.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { OfferModule } from '../offer/offer.module';


@NgModule({
  declarations: [
    CreatePublicationComponent,
    ViewPublicationComponent
  ],
  imports: [
    CommonModule,
    PublicationRoutingModule,
    FormsModule,
    NgbModule,
    OfferModule
  ],
  exports : [
    CreatePublicationComponent,
    ViewPublicationComponent
  ]
})
export class PublicationModule { }
