import { Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Page } from 'src/app/core/models/Page';
import { Publication } from 'src/app/core/models/Publication';
import { PublicationFilter } from 'src/app/core/models/PublicationFilter';
import { HttpService } from 'src/app/core/services/http.service';
import { PublicationService } from 'src/app/core/services/publication.service';
import { Observable, of } from "rxjs";

@Component({
  selector: 'app-home-screen',
  templateUrl: './home-screen.component.html',
  styleUrls: ['./home-screen.component.css']
})
export class HomeScreenComponent implements OnInit {

  currentPage : Page;
  currentFilter : PublicationFilter;
  showFilter : boolean = false;
 

  constructor(public httpService : HttpService, public publicationService : PublicationService) { }

  ngOnInit(): void {
    this.publicationService.pageSubject.subscribe(data => {
      this.currentPage = data;
    });

    this.publicationService.filterSubject.subscribe(data => {
      this.currentFilter = data;
    });

    var filter = new PublicationFilter();
    filter.pageSize = 10;
    filter.pageNumber = 0;
    filter.status = ['OPEN','SETTLING'];

    this.httpService.GetPublications(filter).subscribe(data => {
      this.publicationService.setPage(data);
      this.publicationService.setFilter(filter);
    });
  }

  nextPage(){
    this.currentFilter.pageNumber++;
    this.httpService.GetPublications(this.currentFilter).subscribe(data => {
      this.publicationService.setPage(data);
      this.publicationService.setFilter(this.currentFilter);
    });
  }

  previousPage(){
    this.currentFilter.pageNumber--;
    this.httpService.GetPublications(this.currentFilter).subscribe(data => {
      this.publicationService.setPage(data);
      this.publicationService.setFilter(this.currentFilter);
    });
  }

  onFilter(event : any){
    if(event == 'reset'){
      var filter = new PublicationFilter();
      filter.pageSize = 10;
      filter.pageNumber = 0;
      filter.status = ['OPEN','SETTLING'];
  
      this.httpService.GetPublications(filter).subscribe(data => {
        this.publicationService.setPage(data);
        this.publicationService.setFilter(filter);
      });
    }
    console.log(event);
    this.currentFilter.pageNumber = 0;
    if(event.maxUcuCoins)
      this.currentFilter.maxUcuCoins = event.maxUcuCoins;
    if(event.minUcuCoins)
      this.currentFilter.minUcuCoins = event.minUcuCoins;
    if(event.afterDate){
      this.currentFilter.afterDate = new Date(event.afterDate).toISOString().slice(0, 19).replace('T', ' ');
    }
      
    if(event.beforeDate){
      this.currentFilter.beforeDate = new Date(event.beforeDate).toISOString().slice(0, 19).replace('T', ' ');
    }
      
    this.httpService.GetPublications(this.currentFilter).subscribe(data => {
      this.publicationService.setPage(data);
      this.publicationService.setFilter(this.currentFilter);
    });
  }

  onToggleFilter(event:any){
    event.stopPropagation();
    this.showFilter = !this.showFilter;
  }
}
