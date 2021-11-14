import { Component, OnInit } from '@angular/core';
import { PublicationWrapper } from 'src/app/core/models/PublicationWrapper';
import { ActivatedRoute } from '@angular/router'
import { HttpService } from 'src/app/core/services/http.service';
import { PublicationFilter } from 'src/app/core/models/PublicationFilter';
import { UserService } from 'src/app/core/services/user.service';
import { OfferFilter } from 'src/app/core/models/OfferFilter';
import { OfferWrapper } from 'src/app/core/models/OfferWrapper';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-view-publication',
  templateUrl: './view-publication.component.html',
  styleUrls: ['./view-publication.component.css']
})
export class ViewPublicationComponent implements OnInit {
  wrapper: PublicationWrapper;
  offersWrapper : OfferWrapper[];
  showOffer : boolean = false; 
  showAccountData : boolean = false;
  accountEmail : BehaviorSubject<string> = new BehaviorSubject(null);
  
  constructor(public httpService : HttpService, public route: ActivatedRoute, public user : UserService) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      var filter = new PublicationFilter();
      filter.idPublication = params['id'];
      this.httpService.GetPublications(filter).subscribe(data => {
        this.wrapper = data.content[0];
        if(this.user.isLogged()){
          if(this.user.user.email == data.content[0].publication.accountEmail){
            var offerFilter = new OfferFilter();
            offerFilter.idPublication = this.wrapper.publication.idPublication;
            offerFilter.status = ['OPEN','SETTLING','CHANGED'];
            this.httpService.GetOffers(offerFilter).subscribe(data => {
              this.offersWrapper = data.content;
            });
          }
        }
      });
    });
  }

  toggleShowOfferCreation(){
    this.showOffer = !this.showOffer;
  }

  toggleshowAccountData(){
    this.showAccountData = !this.showAccountData;
  }

  AcceptOffer(idOffer : number){
    this.httpService.AcceptOffer(idOffer).subscribe(data => {

    });
  }

  RejectOffer(idOffer : number){
    this.httpService.RejectOffer(idOffer).subscribe(data => {

    });
  }

  RevertOffer(idOffer : number){
    this.httpService.RevertOffer(idOffer).subscribe(data => {

    });
  }

  ViewAccountData(email : string){
    this.toggleshowAccountData();
    this.accountEmail.next(email);
   
  }

}
