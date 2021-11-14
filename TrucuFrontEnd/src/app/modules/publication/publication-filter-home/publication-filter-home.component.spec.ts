import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicationFilterHomeComponent } from './publication-filter-home.component';

describe('PublicationFilterHomeComponent', () => {
  let component: PublicationFilterHomeComponent;
  let fixture: ComponentFixture<PublicationFilterHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PublicationFilterHomeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PublicationFilterHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
