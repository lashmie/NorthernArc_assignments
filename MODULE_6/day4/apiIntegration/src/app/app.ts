import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ShowEmployee } from './components/show-employee/show-employee';
import { ShowEmployees } from './components/show-employees/show-employees';

@Component({
  selector: 'app-root',
  imports: [ShowEmployees,ShowEmployee],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('apiIntegration');
}
