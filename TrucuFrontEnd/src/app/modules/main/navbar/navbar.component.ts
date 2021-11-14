import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { PublicationFilter } from 'src/app/core/models/PublicationFilter';
import { HttpService } from 'src/app/core/services/http.service';
import { PublicationService } from 'src/app/core/services/publication.service';
import { UserService } from 'src/app/core/services/user.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  @Output() collapseShowEvent = new EventEmitter<boolean>();
  collapseShow:boolean = true;
  isLogged:boolean = false;

  searchText : string;

  showLogin : boolean = false;

  constructor(public publicationService : PublicationService, public http : HttpService, public userService : UserService) { 
    
  }

  ngOnInit(): void {
    this.collapseShowEvent.emit(this.collapseShow);
    this.userService.userSubject.subscribe(data => {
      if(data != null && data.email != null){
        this.isLogged = true;
        this.showLogin = false;
      }
    });

  }

  toggleSideBar(event:any){
    this.collapseShowEvent.emit(!this.collapseShow);
  }  

  searchPublication(){
    var filter = this.publicationService.currentFilter;
    if(this.searchText)
      filter.title = this.searchText;
    filter.pageSize = 10;
    filter.pageNumber = 0;

    this.http.GetPublications(filter).subscribe(data => {
      this.publicationService.setPage(data);
      this.publicationService.setFilter(filter);
    });
  }

  toggleLoginPopup(){
    this.showLogin = !this.showLogin;
  }
}
