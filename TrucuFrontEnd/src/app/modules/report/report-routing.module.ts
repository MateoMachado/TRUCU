import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ViewPublicationComponent } from '../publication/view-publication/view-publication.component';
import { ViewReportsComponent } from './view-reports/view-reports.component';

const routes: Routes = [
  {
    path: '',
    component: ViewReportsComponent,
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
export class ReportRoutingModule { }
