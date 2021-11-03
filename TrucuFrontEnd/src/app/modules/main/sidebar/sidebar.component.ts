import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  @Input() collapseShow: boolean;
  showOptionsTrip=false;

  constructor() { }

  ngOnInit(): void {
  }

  toggleOptionsTrip(){
    this.showOptionsTrip = !this.showOptionsTrip;
  }
}
