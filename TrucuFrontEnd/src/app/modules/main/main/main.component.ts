import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  collapseShow:boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

  toggleSideBar(event:any){
    this.collapseShow = !this.collapseShow;
  }

}
