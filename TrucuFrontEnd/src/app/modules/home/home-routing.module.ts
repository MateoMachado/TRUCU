import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ViewPublicationComponent } from '../publication/view-publication/view-publication.component';
import { HomeScreenComponent } from './home-screen/home-screen.component';

const routes: Routes = [
  {
    path: '',
    component: HomeScreenComponent,
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
export class HomeRoutingModule { }
