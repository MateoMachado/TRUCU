import { Component, Input, OnInit } from '@angular/core';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { Publication } from 'src/app/core/models/Publication';
import { Reason } from 'src/app/core/models/Reason';
import { Report } from 'src/app/core/models/Report';
import { HttpService } from 'src/app/core/services/http.service';

@Component({
  selector: 'app-make-report',
  templateUrl: './make-report.component.html',
  styleUrls: ['./make-report.component.css']
})
export class MakeReportComponent implements OnInit {
  @Input() publication : Publication;
  @Input() showModal : boolean = false;
  reasons : Reason[];
  selectedReason : Reason;

  constructor(public http : HttpService, public toastr : ToastrService) { 
    this.http.GetReasons().subscribe(data => {
      this.reasons = data;
    });
  }

  ngOnInit(): void {
  }

  onSelectReason(event : any){
    this.selectedReason = event;
  }

  report(){
    var report = new Report();
    report.idReason = this.selectedReason.idReason;
    report.idPublication = this.publication.idPublication;
    
    this.http.CreateReport(report).subscribe(data => {
      this.showModal = false;
      this.toastr.success("Publicacion reportada correctamente");
    })
  }

}
