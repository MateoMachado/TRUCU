import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Page } from '../models/Page';
import { PublicationFilter } from '../models/PublicationFilter';

@Injectable({
  providedIn: 'root'
})
export class PublicationService {
  page : Page;
  currentFilter : PublicationFilter;

  pageSubject = new BehaviorSubject<Page>(new Page);
  filterSubject = new BehaviorSubject<PublicationFilter>(new PublicationFilter);


  constructor() {
    this.pageSubject.subscribe(data => this.page = data, error => console.log(error));
    this.filterSubject.subscribe(data => this.currentFilter = data, error => console.log(error));
  }

  setPage(value : Page){
    this.pageSubject.next(value);
  }

  setFilter(value : PublicationFilter){
    this.filterSubject.next(value);
  }

  
}
