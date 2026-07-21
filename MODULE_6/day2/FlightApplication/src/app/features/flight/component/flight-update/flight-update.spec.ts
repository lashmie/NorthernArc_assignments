import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FlightUpdate } from './flight-update';

describe('FlightUpdate', () => {
  let component: FlightUpdate;
  let fixture: ComponentFixture<FlightUpdate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FlightUpdate],
    }).compileComponents();

    fixture = TestBed.createComponent(FlightUpdate);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
