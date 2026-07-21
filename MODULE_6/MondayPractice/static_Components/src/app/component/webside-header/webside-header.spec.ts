import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WebsideHeader } from './webside-header';

describe('WebsideHeader', () => {
  let component: WebsideHeader;
  let fixture: ComponentFixture<WebsideHeader>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WebsideHeader],
    }).compileComponents();

    fixture = TestBed.createComponent(WebsideHeader);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
