import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreateOfferComponent } from './create-offer/create-offer.component';
import { ViewMyOffersComponent } from './view-my-offers/view-my-offers.component';
import { OfferRoutingModule } from './offer-routing.module';
import { AccountModule } from '../account/account.module';
import { CreateCounterOfferComponent } from './create-counter-offer/create-counter-offer.component';



@NgModule({
  declarations: [
    CreateOfferComponent,
    ViewMyOffersComponent,
    CreateCounterOfferComponent
  ],
  imports: [
    CommonModule,
    OfferRoutingModule,
    AccountModule
  ],
  exports: [
    CreateOfferComponent,
    CreateCounterOfferComponent
  ]
})
export class OfferModule { }
