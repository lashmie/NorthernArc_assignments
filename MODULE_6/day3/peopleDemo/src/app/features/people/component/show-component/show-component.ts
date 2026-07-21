import { Component, inject } from '@angular/core';
import { PeopleSservice } from '../../services/people-service';
import { AsyncPipe } from '@angular/common';
import { AddComponent } from '../add-component/add-component/add-component';

@Component({
  selector: 'app-show-component',
  imports: [AsyncPipe],
  templateUrl: './show-component.html',
  styleUrl: './show-component.css',
})
export class ShowComponent {
  peoppleService:PeopleSservice=inject(PeopleSservice);
}
