import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreateOfferComponent } from './create-offer/create-offer.component';



@NgModule({
  declarations: [
    CreateOfferComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    CreateOfferComponent
  ]
})
export class OfferModule { }
