import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ViewPublicationComponent } from '../publication/view-publication/view-publication.component';
import { ViewMyOffersComponent } from './view-my-offers/view-my-offers.component';


const routes: Routes = [
  {
    path: 'viewMyOffers',
    component: ViewMyOffersComponent,
  },
  {
    path: 'viewPublication/:id',
    component: ViewPublicationComponent
  }  
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OfferRoutingModule { }
