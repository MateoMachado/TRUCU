import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreatePublicationComponent } from './create-publication/create-publication.component';
import { ViewPublicationComponent } from './view-publication/view-publication.component';

const routes: Routes = [
  {
    path: '',
    component: CreatePublicationComponent,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PublicationRoutingModule { }
