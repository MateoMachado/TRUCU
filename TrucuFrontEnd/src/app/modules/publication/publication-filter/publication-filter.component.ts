import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { SelectorComponent } from 'src/app/core/components/selector/selector.component';

@Component({
  selector: 'app-publication-filter',
  templateUrl: './publication-filter.component.html',
  styleUrls: ['./publication-filter.component.css']
})
export class PublicationFilterComponent implements OnInit {
  @Output() filteredElementsEvent:EventEmitter<any> = new EventEmitter<any>();
  @Input() showModal : boolean;
  statuses : string[] = ['ABIERTAS','ACEPTADAS','OCULTAS','CERRADAS','REPORTADAS','CANCELADAS'];
  selectedStatus : string;

  @ViewChild('statusSelector') statusSelector : SelectorComponent;
  constructor(public toastr : ToastrService) { }

  ngOnInit(): void {
  }

  selectStatus(event : string){
    this.selectedStatus = event;
  }

  closeSelectors(event:Event){
    event.stopPropagation();
    if(this.statusSelector)
     this.statusSelector.closeSelector();
  }

  reset(){
    this.resetForm();
    this.filteredElementsEvent.emit("reset");
    this.toastr.info('Filtro reiniciado','Informacion');
  }

  resetForm(){
    if(this.statusSelector)
      this.statusSelector.reset();
    this.selectedStatus = '';
  }

  search(){
    var palabra = "";
    switch(this.selectedStatus){
      case "ABIERTAS": 
        palabra = "OPEN";  
        break;
      case "OCULTAS": 
        palabra = "HIDDEN";  
        break;
      case "CERRADAS": 
        palabra = "CLOSED";  
        break;
      case "ACEPTADAS": 
        palabra = "SETTLING";  
        break;
      case "REPORTADAS": 
        palabra = "REPORTED";  
        break;
      case "CANCELADAS": 
        palabra = "CANCELED";  
        break;
    }
    this.filteredElementsEvent.emit(palabra);
  }
}
