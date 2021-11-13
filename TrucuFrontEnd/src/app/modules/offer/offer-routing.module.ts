import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ViewMyOffersComponent } from './view-my-offers/view-my-offers.component';


const routes: Routes = [
  {
    path: 'viewMyOffers',
    component: ViewMyOffersComponent,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OfferRoutingModule { }
