import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ShowCount } from './features/components/show-count/show-count';
import { Increment } from './features/components/increment/increment';
import { Decrement } from './features/components/decrement/decrement';

@Component({
  selector: 'app-root',
  imports: [ShowCount,Increment,Decrement],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('CounterDemo');
}
