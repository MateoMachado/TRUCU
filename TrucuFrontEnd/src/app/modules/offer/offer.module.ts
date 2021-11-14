import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreateOfferComponent } from './create-offer/create-offer.component';
import { ViewMyOffersComponent } from './view-my-offers/view-my-offers.component';
import { OfferRoutingModule } from './offer-routing.module';
import { AccountModule } from '../account/account.module';



@NgModule({
  declarations: [
    CreateOfferComponent,
    ViewMyOffersComponent
  ],
  imports: [
    CommonModule,
    OfferRoutingModule,
    AccountModule
  ],
  exports: [
    CreateOfferComponent
  ]
})
export class OfferModule { }
