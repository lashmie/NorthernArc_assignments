import { Component, inject } from '@angular/core';
import { CounterServices } from '../../services/counter-services';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-show-component',
  imports: [AsyncPipe],
  templateUrl: './show-component.html',
  styleUrl: './show-component.css',
})
export class ShowComponent {
  // countService: CountService=inject(CountService);
  counterService: CounterServices=inject(CounterServices);
}
