import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewMyOffersComponent } from './view-my-offers.component';

describe('ViewMyOffersComponent', () => {
  let component: ViewMyOffersComponent;
  let fixture: ComponentFixture<ViewMyOffersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewMyOffersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewMyOffersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
