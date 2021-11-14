import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreatePublicationComponent } from './create-publication/create-publication.component';
import { PublicationRoutingModule } from './publication-routing.module';
import { FormsModule } from '@angular/forms';
import { ViewPublicationComponent } from './view-publication/view-publication.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { OfferModule } from '../offer/offer.module';
import { PublicationListComponent } from './publication-list/publication-list.component';
import { AccountModule } from '../account/account.module';
import { ReportModule } from '../report/report.module';


@NgModule({
  declarations: [
    CreatePublicationComponent,
    ViewPublicationComponent,
    PublicationListComponent
  ],
  imports: [
    CommonModule,
    PublicationRoutingModule,
    FormsModule,
    NgbModule,
    OfferModule,
    AccountModule,
    ReportModule
  ],
  exports : [
    CreatePublicationComponent,
    ViewPublicationComponent,
    PublicationListComponent
  ]
})
export class PublicationModule { }
