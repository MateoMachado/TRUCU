import { Component, OnInit } from '@angular/core';
import { Account } from 'src/app/core/models/Account';

@Component({
  selector: 'app-create-publication',
  templateUrl: './create-publication.component.html',
  styleUrls: ['./create-publication.component.css']
})
export class CreatePublicationComponent implements OnInit {
  files: File[] | null = null;
  account : Account = new Account;
  confirmPassword : string;

  constructor() { }

  ngOnInit(): void {
  }

  handleFileInput(files: FileList) {
    if(this.files == null){
      this.files = [];
    }
    this.files.push(files.item(0));
  }

  removeFile(index : number){
    this.files.splice(index,1);
  }

}
