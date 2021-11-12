import { Component, OnInit } from '@angular/core';
import { PublicationWrapper } from 'src/app/core/models/PublicationWrapper';
import { ActivatedRoute } from '@angular/router'
import { HttpService } from 'src/app/core/services/http.service';
import { PublicationFilter } from 'src/app/core/models/PublicationFilter';

@Component({
  selector: 'app-view-publication',
  templateUrl: './view-publication.component.html',
  styleUrls: ['./view-publication.component.css']
})
export class ViewPublicationComponent implements OnInit {
  wrapper: PublicationWrapper;
  images = [944, 1011, 984].map((n) => `https://picsum.photos/id/${n}/900/500`);
  
  constructor(public httpService : HttpService, public route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
        var filter = new PublicationFilter();
        filter.idPublication = params['id'];
        this.httpService.GetPublications(filter).subscribe(data => {
          this.wrapper = data.content[0];
          console.log(this.wrapper);
        });
    });
  }

}
