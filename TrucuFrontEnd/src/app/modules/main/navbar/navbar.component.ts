import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  @Output() collapseShowEvent = new EventEmitter<boolean>();
  collapseShow:boolean = true;

  constructor() { }

  ngOnInit(): void {
    this.collapseShowEvent.emit(this.collapseShow);
  }

  toggleSideBar(event:any){
    this.collapseShowEvent.emit(!this.collapseShow);
  }  
}
