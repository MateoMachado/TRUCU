import { Component, Input, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject } from 'rxjs';
import { Offer } from 'src/app/core/models/Offer';
import { Page } from 'src/app/core/models/Page';
import { Publication } from 'src/app/core/models/Publication';
import { PublicationFilter } from 'src/app/core/models/PublicationFilter';
import { HttpService } from 'src/app/core/services/http.service';

@Component({
  selector: 'app-create-counter-offer',
  templateUrl: './create-counter-offer.component.html',
  styleUrls: ['./create-counter-offer.component.css']
})
export class CreateCounterOfferComponent implements OnInit {
  @Input() accountEmail : BehaviorSubject<string>;
  @Input() showModal: boolean;
  @Input() offer : Offer;

  selectedPublications : Map<number, Publication> = new Map<number, Publication>();

  ownerPublications: Publication[] = [];

  constructor(public http: HttpService, public toastr: ToastrService) { }

  ngOnInit(): void {
    this.accountEmail.subscribe(value => {
      var filter = new PublicationFilter;

      filter.accountEmail = value;
      filter.status = ['OPEN'];
      if(value != null){
        this.http.GetPublications(filter).subscribe(data => {
          data.content.forEach(data => {
            this.ownerPublications.push(data.publication);
          })
        });
      }
    });

  }

  checkPublication(publication : Publication){
    if(this.selectedPublications.has(publication.idPublication)){
      this.selectedPublications.delete(publication.idPublication);
    }else{
      this.selectedPublications.set(publication.idPublication,publication);
    }
  }

  toggleModal() {
    this.showModal = !this.showModal;
  }

  makeCounterOffer(){
    var publications = [];
    this.selectedPublications.forEach(data => {
      publications.push(data.idPublication);
    });
    this.http.MakeCounterOffer(this.offer.idOffer,publications).subscribe(data => {
      this.toastr.success('Se creo la contra oferta', 'Exito');
      this.toggleModal();
    });
  }
}
