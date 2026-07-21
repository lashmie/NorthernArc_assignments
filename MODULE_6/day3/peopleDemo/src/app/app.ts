import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ShowComponent } from './features/people/component/show-component/show-component';
import { AddComponent } from './features/people/component/add-component/add-component/add-component';
import { UpdateComponent } from './features/people/component/update-component/update-component';
import { ShowBook } from './features/book/components/show-book/show-book';
import { AddBook } from './features/book/components/add-book/add-book';
import { UpdateBook } from './features/book/components/update-book/update-book';

@Component({
  selector: 'app-root',
  // imports: [ShowComponent, AddComponent, UpdateComponent],
    imports: [ShowBook,AddBook,UpdateBook],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('peopleDemo');
}
