import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './main/main.component';

const routes: Routes = [
    {
    path: '', component: MainComponent ,children: [
        {
            path: '',
            loadChildren: () => import('../home/home.module').then(m => m.HomeModule)
        },  
        {
            path: 'home',
            loadChildren: () => import('../home/home.module').then(m => m.HomeModule)
        },   
        {
            path: 'createPublication',
            loadChildren: () => import('../publication/publication.module').then(m => m.PublicationModule)
        },
        {
            path: 'viewMyPublications',
            loadChildren: () => import('../publication/publication.module').then(m => m.PublicationModule)
        },
        {
            path: 'viewMyOffers',
            loadChildren: () => import('../offer/offer.module').then(m => m.OfferModule)
        },
        
    ]}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MainRoutingModule { }
