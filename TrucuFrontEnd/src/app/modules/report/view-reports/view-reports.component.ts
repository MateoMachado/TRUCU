import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Page } from 'src/app/core/models/Page';
import { PublicationFilter } from 'src/app/core/models/PublicationFilter';
import { Reason } from 'src/app/core/models/Reason';
import { HttpService } from 'src/app/core/services/http.service';
import { PublicationService } from 'src/app/core/services/publication.service';

@Component({
  selector: 'app-view-reports',
  templateUrl: './view-reports.component.html',
  styleUrls: ['./view-reports.component.css']
})
export class ViewReportsComponent implements OnInit {
  reasons : Reason[];
  currentPage : Page;
  currentFilter : PublicationFilter;
  reportsReasons : Map<number,Map<number,number>> = new Map<number,Map<number,number>>();
 
  constructor(public httpService : HttpService, public publicationService : PublicationService, public toastr : ToastrService) { }

  ngOnInit(): void {
    this.httpService.GetReasons().subscribe(data => {
      this.reasons = data;
    });

    this.publicationService.pageSubject.subscribe(data => {
      this.currentPage = data;
      this.reportsReasons.clear();
      this.currentPage.content.forEach(publication => {
        this.httpService.GetReportReasons(publication.publication.idPublication).subscribe(data => {
          this.reportsReasons.set(publication.publication.idPublication, data);
        });
      });
    });

    this.publicationService.filterSubject.subscribe(data => {
      this.currentFilter = data;
    });

    var filter = new PublicationFilter();
    filter.pageSize = 10;
    filter.pageNumber = 0;

    this.httpService.GetReportedPublications(filter).subscribe(data => {
      this.publicationService.setPage(data);
      this.publicationService.setFilter(filter);
    });
  }

  nextPage(){
    this.currentFilter.pageNumber++;
    this.httpService.GetPublications(this.currentFilter).subscribe(data => {
      this.publicationService.setPage(data);
      this.publicationService.setFilter(this.currentFilter);
    });
  }

  acceptReport(idPublication : number){
    this.httpService.AcceptReport(idPublication).subscribe(data => {
      this.toastr.success('Publicacion reportada con exito');
    });
  }

  rejectReport(idPublication : number){
    this.httpService.CancelReport(idPublication).subscribe(data => {
      this.toastr.success('Reporte rechazado con exito');
    });
  }  

}
