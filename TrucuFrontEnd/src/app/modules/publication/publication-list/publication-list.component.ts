import { Component, OnInit } from '@angular/core';
import { Page } from 'src/app/core/models/Page';
import { PublicationFilter } from 'src/app/core/models/PublicationFilter';
import { HttpService } from 'src/app/core/services/http.service';
import { PublicationService } from 'src/app/core/services/publication.service';
import { UserService } from 'src/app/core/services/user.service';

@Component({
  selector: 'app-publication-list',
  templateUrl: './publication-list.component.html',
  styleUrls: ['./publication-list.component.css']
})
export class PublicationListComponent implements OnInit {

  currentPage : Page;
  currentFilter : PublicationFilter;
 

  constructor(public httpService : HttpService, public publicationService : PublicationService, public user : UserService) { }

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
    this.user.userSubject.subscribe(data => {
      filter.accountEmail = data.email; 
    });

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

}