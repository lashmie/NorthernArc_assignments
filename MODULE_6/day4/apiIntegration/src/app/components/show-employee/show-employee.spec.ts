import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowEmployee } from './show-employee';

describe('ShowEmployee', () => {
  let component: ShowEmployee;
  let fixture: ComponentFixture<ShowEmployee>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowEmployee],
    }).compileComponents();

    fixture = TestBed.createComponent(ShowEmployee);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
