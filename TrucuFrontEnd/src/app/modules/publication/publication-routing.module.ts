import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreatePublicationComponent } from './create-publication/create-publication.component';
import { PublicationListComponent } from './publication-list/publication-list.component';
import { ViewPublicationComponent } from './view-publication/view-publication.component';

const routes: Routes = [
  {
    path: '',
    component: CreatePublicationComponent,
  },
  {
    path: 'viewMyPublications',
    component: PublicationListComponent,
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
export class PublicationRoutingModule { }
