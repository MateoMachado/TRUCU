import { Component, Input, OnInit } from '@angular/core';
import { UserService } from 'src/app/core/services/user.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  @Input() collapseShow: boolean;
  showOptionsTrip=false;

  constructor(public user : UserService) { }

  ngOnInit(): void {
  }

  toggleOptionsTrip(){
    this.showOptionsTrip = !this.showOptionsTrip;
  }
}
