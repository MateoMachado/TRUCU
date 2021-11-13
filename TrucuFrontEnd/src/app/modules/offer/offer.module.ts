import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreateOfferComponent } from './create-offer/create-offer.component';
import { ViewMyOffersComponent } from './view-my-offers/view-my-offers.component';
import { OfferRoutingModule } from './offer-routing.module';



@NgModule({
  declarations: [
    CreateOfferComponent,
    ViewMyOffersComponent
  ],
  imports: [
    CommonModule,
    OfferRoutingModule
  ],
  exports: [
    CreateOfferComponent
  ]
})
export class OfferModule { }
