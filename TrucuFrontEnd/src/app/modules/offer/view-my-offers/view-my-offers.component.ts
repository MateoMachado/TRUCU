import { Component, OnInit } from '@angular/core';
import { OfferFilter } from 'src/app/core/models/OfferFilter';
import { Page } from 'src/app/core/models/Page';
import { HttpService } from 'src/app/core/services/http.service';
import { UserService } from 'src/app/core/services/user.service';

@Component({
  selector: 'app-view-my-offers',
  templateUrl: './view-my-offers.component.html',
  styleUrls: ['./view-my-offers.component.css']
})
export class ViewMyOffersComponent implements OnInit {
  currentPage : Page;
  offerFilter : OfferFilter;

  constructor(public http : HttpService, public user : UserService) { }

  ngOnInit(): void {
    this.offerFilter = new OfferFilter();
    var email = this.user.user.email;
    this.offerFilter.accountEmail = email;
    this.offerFilter.pageSize = 10;
    this.offerFilter.pageNumber = 0;
    
    this.http.GetUserOffers(this.offerFilter).subscribe(data => {
      this.currentPage = data;
    });
  }

  cancelOffer(idOffer : number){
    this.http.CancelOffer(idOffer).subscribe(data => {});
  }

  nextPage(){
    this.offerFilter.pageNumber++;
    this.http.GetUserOffers(this.offerFilter).subscribe(data => {
      this.currentPage = data;
    });
  }

}
