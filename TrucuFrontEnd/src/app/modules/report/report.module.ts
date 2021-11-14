import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MakeReportComponent } from './make-report/make-report.component';
import { SharedModule } from 'src/app/core/components/shared.module';
import { ViewReportsComponent } from './view-reports/view-reports.component';
import { ReportRoutingModule } from './report-routing.module';
import { PublicationModule } from '../publication/publication.module';



@NgModule({
  declarations: [
    MakeReportComponent,
    ViewReportsComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    ReportRoutingModule
  ],
  exports: [
    MakeReportComponent,
    ViewReportsComponent
  ]
})
export class ReportModule { }
