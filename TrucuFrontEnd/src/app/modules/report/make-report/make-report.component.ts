import { Component, Input, OnInit } from '@angular/core';
import { Publication } from 'src/app/core/models/Publication';
import { Reason } from 'src/app/core/models/Reason';
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

  constructor(public http : HttpService) { 
  }

  ngOnInit(): void {
  }

  onSelectReason(event : any){
    this.selectedReason = event;
  }

}
