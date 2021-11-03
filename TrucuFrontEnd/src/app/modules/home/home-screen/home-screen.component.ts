import { Component, OnInit } from '@angular/core';
import { Page } from 'src/app/core/models/Page';
import { Publication } from 'src/app/core/models/Publication';
import { PublicationFilter } from 'src/app/core/models/PublicationFilter';
import { HttpService } from 'src/app/core/services/http.service';
import { PublicationService } from 'src/app/core/services/publication.service';

@Component({
  selector: 'app-home-screen',
  templateUrl: './home-screen.component.html',
  styleUrls: ['./home-screen.component.css']
})
export class HomeScreenComponent implements OnInit {

  currentPage : Page;
  currentFilter : PublicationFilter;

  constructor(public httpService : HttpService, public publicationService : PublicationService) { }

  ngOnInit(): void {
    this.publicationService.pageSubject.subscribe(data => {
      this.currentPage = data;
    });

    this.publicationService.filterSubject.subscribe(data => {
      this.currentFilter = data;
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
