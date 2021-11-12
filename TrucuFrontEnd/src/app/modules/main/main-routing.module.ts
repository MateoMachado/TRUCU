import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './main/main.component';

const routes: Routes = [
    {
    path: '', component: MainComponent ,children: [
        {
            path: 'home',
            loadChildren: () => import('../home/home.module').then(m => m.HomeModule)
        },   
        {
            path: 'createPublication',
            loadChildren: () => import('../publication/publication.module').then(m => m.PublicationModule)
        },
    ]}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MainRoutingModule { }
