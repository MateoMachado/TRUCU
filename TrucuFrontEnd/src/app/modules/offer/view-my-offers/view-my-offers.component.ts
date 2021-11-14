import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { element } from 'protractor';
import { BehaviorSubject } from 'rxjs';
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
  showAccountData : boolean = false;
  accountEmail : BehaviorSubject<string> = new BehaviorSubject(null);
  

  constructor(public http : HttpService, public user : UserService, public toastr : ToastrService) { }

  ngOnInit(): void {
    this.offerFilter = new OfferFilter();
    var email = this.user.user.email;
    this.offerFilter.accountEmail = email;
    this.offerFilter.pageSize = 10;
    this.offerFilter.pageNumber = 0;
    this.offerFilter.status = ['OPEN','SETTLING','CHANGED'];
    this.http.GetOffers(this.offerFilter).subscribe(data => {
      this.currentPage = data;
    });
   
  }

  cancelOffer(idOffer : number){
    this.http.CancelOffer(idOffer).subscribe(data => {
      this.toastr.success('Cancelado correctamente');
      this.http.GetOffers(this.offerFilter).subscribe(data => {
        this.currentPage = data;
      });
    });
  }

  closeOffer(idOffer : number){
    this.http.CloseOffer(idOffer).subscribe(data => {
      this.toastr.success('Oferta finalizada correctamente');
      this.http.GetOffers(this.offerFilter).subscribe(data => {
        this.currentPage = data;
      });
    });
  }

  acceptCounterOffer(idOffer : number){
    this.http.AcceptCounterOffer(idOffer).subscribe(data => {
      this.toastr.success('Contra oferta aceptada correctamente','Exito');
      this.http.GetOffers(this.offerFilter).subscribe(data => {
        this.currentPage = data;
      });
    });
  }

  rejectCounterOffer(idOffer : number){
    this.http.RejectCounterOffer(idOffer).subscribe(data => {
      this.toastr.success('Contra oferta rechazada correctamente','Exito');
      this.http.GetOffers(this.offerFilter).subscribe(data => {
        this.currentPage = data;
      });
    });
  }

  nextPage(){
    this.offerFilter.pageNumber++;
    this.http.GetOffers(this.offerFilter).subscribe(data => {
      this.currentPage = data;
    });
  }

  previousPage(){
    this.offerFilter.pageNumber--;
    this.http.GetOffers(this.offerFilter).subscribe(data => {
      this.currentPage = data;
    });
  }

  toggleshowAccountData(){
    this.showAccountData = !this.showAccountData;
  }

  ViewAccountData(email : string){
    this.toggleshowAccountData();
    this.accountEmail.next(email);
  }
}
