import { Component, OnInit } from '@angular/core';
import { PublicationWrapper } from 'src/app/core/models/PublicationWrapper';
import { ActivatedRoute } from '@angular/router'
import { HttpService } from 'src/app/core/services/http.service';
import { PublicationFilter } from 'src/app/core/models/PublicationFilter';
import { UserService } from 'src/app/core/services/user.service';
import { OfferFilter } from 'src/app/core/models/OfferFilter';
import { OfferWrapper } from 'src/app/core/models/OfferWrapper';
import { BehaviorSubject } from 'rxjs';
import { Offer } from 'src/app/core/models/Offer';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-view-publication',
  templateUrl: './view-publication.component.html',
  styleUrls: ['./view-publication.component.css']
})
export class ViewPublicationComponent implements OnInit {
  wrapper: PublicationWrapper;
  offersWrapper : OfferWrapper[];
  showOffer : boolean = false; 
  showReport : boolean = false; 
  showCounterOffer : boolean = false;
  showAccountData : boolean = false;
  accountEmail : BehaviorSubject<string> = new BehaviorSubject(null);
  currentOffer : Offer;
  
  constructor(public httpService : HttpService, public route: ActivatedRoute, public user : UserService, public toast : ToastrService) { }

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

  toggleShowReport(){
    this.showReport = !this.showReport;
  }

  toggleShowCounterOffer(){
    this.showCounterOffer = !this.showCounterOffer;
  }

  toggleshowAccountData(){
    this.showAccountData = !this.showAccountData;
  }

  AcceptOffer(idOffer : number){
    this.httpService.AcceptOffer(idOffer).subscribe(data => {
      this.toast.success('Oferta aceptada con exito');
      var offerFilter = new OfferFilter();
      offerFilter.idPublication = this.wrapper.publication.idPublication;
      offerFilter.status = ['OPEN','SETTLING','CHANGED'];
      this.httpService.GetOffers(offerFilter).subscribe(data => {
        this.offersWrapper = data.content;
      });
    });
  }

  RejectOffer(idOffer : number){
    this.httpService.RejectOffer(idOffer).subscribe(data => {
      this.toast.success('Oferta rechazada', 'Exito');
      var offerFilter = new OfferFilter();
      offerFilter.idPublication = this.wrapper.publication.idPublication;
      offerFilter.status = ['OPEN','SETTLING','CHANGED'];
      this.httpService.GetOffers(offerFilter).subscribe(data => {
        this.offersWrapper = data.content;
      });
    });
  }

  RevertOffer(idOffer : number){
    this.httpService.RevertOffer(idOffer).subscribe(data => {
      this.toast.success('Oferta revertida', 'Exito');
      var offerFilter = new OfferFilter();
      offerFilter.idPublication = this.wrapper.publication.idPublication;
      offerFilter.status = ['OPEN','SETTLING','CHANGED'];
      this.httpService.GetOffers(offerFilter).subscribe(data => {
        this.offersWrapper = data.content;
      });
    });
  }

  ViewAccountData(email : string){
    this.toggleshowAccountData();
    this.accountEmail.next(email);
  }

  CounterOffer(email : string, offer : Offer){
    this.toggleShowCounterOffer();
    this.currentOffer = offer;
    this.accountEmail.next(email);
  }

  hidePublication(){
    this.httpService.HidePublication(this.wrapper.publication.idPublication).subscribe(data => {
      this.toast.success('Se oculto la publicación correctamente, se eliminaron las ofertas relacionadas a ella','Exito');
    })
  }

}
