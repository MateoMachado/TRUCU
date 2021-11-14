import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { SelectorComponent } from 'src/app/core/components/selector/selector.component';
import { PublicationFilter } from 'src/app/core/models/PublicationFilter';

@Component({
  selector: 'app-publication-filter-home',
  templateUrl: './publication-filter-home.component.html',
  styleUrls: ['./publication-filter-home.component.css']
})
export class PublicationFilterHomeComponent implements OnInit {

  @Output() filteredElementsEvent:EventEmitter<any> = new EventEmitter<any>();
  @Input() showModal : boolean;
  statuses : string[] = ['ABIERTAS','ACEPTADAS','OCULTAS','CERRADAS','REPORTADAS','CANCELADAS'];
  selectedStatus : string;
  publicationFilter : PublicationFilter = new PublicationFilter();

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
    this.filteredElementsEvent.emit(this.publicationFilter);
  }

}
