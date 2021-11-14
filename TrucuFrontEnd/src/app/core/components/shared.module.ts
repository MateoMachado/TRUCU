import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SelectorComponent } from './selector/selector.component';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    SelectorComponent
  ],
  imports: [
    CommonModule,
    FormsModule
  ],
  exports: [
    SelectorComponent
  ]
})
export class SharedModule { }
