import { Component, Input, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Page } from 'src/app/core/models/Page';
import { Publication } from 'src/app/core/models/Publication';
import { PublicationFilter } from 'src/app/core/models/PublicationFilter';
import { HttpService } from 'src/app/core/services/http.service';
import { UserService } from 'src/app/core/services/user.service';

@Component({
  selector: 'app-create-offer',
  templateUrl: './create-offer.component.html',
  styleUrls: ['./create-offer.component.css']
})
export class CreateOfferComponent implements OnInit {
  @Input() publication : Publication;
  @Input() showModal : boolean;

  ownerPublications : Page[] = [];
  selectedOffers : Map<number, Publication> = new Map<number, Publication>();
  

  constructor(public httpService : HttpService, public userService : UserService, public toastr : ToastrService) { }

  ngOnInit(): void {
    this.userService.userSubject.subscribe(data => {
      var filter = new PublicationFilter;
      filter.accountEmail = data.email;

      this.httpService.GetPublications(filter).subscribe(data => {
        data.content.forEach(data => {
          this.ownerPublications.push(data.publication);
        })
      });
    })
  }

  toggleModal(){
    this.showModal = !this.showModal;
  }

  checkPublication(publication : Publication){
    if(this.selectedOffers.has(publication.idPublication)){
      this.selectedOffers.delete(publication.idPublication);
    }else{
      this.selectedOffers.set(publication.idPublication,publication);
    }
  }

  createOffer(){
    var publications = [];
    this.selectedOffers.forEach(data => {
      publications.push(data.idPublication);
    });
    this.httpService.CreateOffer(this.publication.idPublication, publications).subscribe(data => {
      this.toastr.success('Exito', 'Se creo la oferta correctamente');
      this.toggleModal();
    });
  }

}
