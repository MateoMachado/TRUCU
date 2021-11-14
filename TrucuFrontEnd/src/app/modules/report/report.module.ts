import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MakeReportComponent } from './make-report/make-report.component';
import { SharedModule } from 'src/app/core/components/shared.module';



@NgModule({
  declarations: [
    MakeReportComponent
  ],
  imports: [
    CommonModule,
    SharedModule
  ],
  exports: [
    MakeReportComponent
  ]
})
export class ReportModule { }
