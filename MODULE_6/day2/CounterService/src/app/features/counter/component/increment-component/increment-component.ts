import { Component, inject } from '@angular/core';
import { CounterServices } from '../../services/counter-services';

@Component({
  selector: 'app-increment-component',
  imports: [],
  templateUrl: './increment-component.html',
  styleUrl: './increment-component.css',
})
export class IncrementComponent {
  counterservice: CounterServices=inject(CounterServices);
}
