import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { People } from './features/people/component/people/people';

@Component({
  selector: 'app-root',
  imports: [People],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('secondDemo');
}
