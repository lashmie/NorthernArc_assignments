import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ShowComponent } from './features/counter/component/show-component/show-component';
import { IncrementComponent } from './features/counter/component/increment-component/increment-component';

@Component({
  selector: 'app-root',
  imports: [ShowComponent, IncrementComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('CounterService');
}
