import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicationFilterComponent } from './publication-filter.component';

describe('PublicationFilterComponent', () => {
  let component: PublicationFilterComponent;
  let fixture: ComponentFixture<PublicationFilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PublicationFilterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PublicationFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
