import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Account } from 'src/app/core/models/Account';
import { HttpService } from 'src/app/core/services/http.service';

@Component({
  selector: 'app-view-account',
  templateUrl: './view-account.component.html',
  styleUrls: ['./view-account.component.css']
})
export class ViewAccountComponent implements OnInit {
  @Input() accountEmail : BehaviorSubject<string>;
  @Input() showModal : boolean;
  @Output() toggle : EventEmitter<any> = new EventEmitter();
  currentAccount : Account;

  constructor(public http : HttpService) { }

  ngOnInit(): void {
    this.accountEmail.subscribe(value => {
      this.http.GetAccount(value).subscribe(data => {
        this.currentAccount = data;
      })
    });
  }

  toggleModal(){
    this.toggle.emit();
  }

}
