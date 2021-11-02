import { Component, OnInit } from '@angular/core';
import { Publication } from 'src/app/core/models/Publication';
import { PublicationFilter } from 'src/app/core/models/PublicationFilter';
import { HttpService } from 'src/app/core/services/http.service';

@Component({
  selector: 'app-home-screen',
  templateUrl: './home-screen.component.html',
  styleUrls: ['./home-screen.component.css']
})
export class HomeScreenComponent implements OnInit {

  publications : Publication[];

  constructor(public httpService : HttpService) { }

  ngOnInit(): void {
    this.httpService.GetPublications(new PublicationFilter).subscribe(x => {
      console.log(x);
      this.publications = x;
    });
  }

}
