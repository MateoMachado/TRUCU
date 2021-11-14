import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './navbar/navbar.component';
import { MainComponent } from './main/main.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { RouterModule } from '@angular/router';
import { MainRoutingModule } from './main-routing.module';
import { HomeModule } from '../home/home.module';
import { FormsModule } from '@angular/forms';
import { LoginModule } from '../login/login.module';
import { PublicationModule } from '../publication/publication.module';
import { ReportModule } from '../report/report.module';

@NgModule({
  declarations: [
    NavbarComponent,
    MainComponent,
    SidebarComponent
  ],
  imports: [
    CommonModule,
    MainRoutingModule,
    HomeModule,
    RouterModule,
    FormsModule,
    LoginModule,
    PublicationModule,
    ReportModule
  ]
})
export class MainModule { }
