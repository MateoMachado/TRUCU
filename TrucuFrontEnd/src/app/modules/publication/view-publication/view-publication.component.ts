import { Component, OnInit } from '@angular/core';
import { PublicationWrapper } from 'src/app/core/models/PublicationWrapper';
import { ActivatedRoute } from '@angular/router'

@Component({
  selector: 'app-view-publication',
  templateUrl: './view-publication.component.html',
  styleUrls: ['./view-publication.component.css']
})
export class ViewPublicationComponent implements OnInit {
  wrapper: PublicationWrapper;

  constructor(public route: ActivatedRoute) { }

  ngOnInit(): void {
    console.log(JSON.parse(this.route.snapshot.paramMap.get('idPublication')));
    // this.wrapper = new PublicationWrapper();
    // this.wrapper.publication = ;
    
  }

}
