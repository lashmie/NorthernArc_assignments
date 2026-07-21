import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ShowComponent } from './features/movies/components/show-component/show-component';
import { AddComponent } from './features/movies/components/add-component/add-component';
import { UpdateComponent } from './features/movies/components/update-component/update-component';
// import { ShowComponent } from './features/namessss/components/show-component/show-component';
// import { AddComponent } from './features/namessss/components/add-component/add-component';
// import { UpdateComponent } from './features/namessss/components/update-component/update-component';

@Component({
  selector: 'app-root',
  imports: [ShowComponent, AddComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('Demos');
}
