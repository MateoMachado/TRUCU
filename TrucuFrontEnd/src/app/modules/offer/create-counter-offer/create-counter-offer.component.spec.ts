import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateCounterOfferComponent } from './create-counter-offer.component';

describe('CreateCounterOfferComponent', () => {
  let component: CreateCounterOfferComponent;
  let fixture: ComponentFixture<CreateCounterOfferComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateCounterOfferComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateCounterOfferComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
