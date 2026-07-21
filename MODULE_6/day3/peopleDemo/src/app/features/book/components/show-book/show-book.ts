import { Component, inject } from '@angular/core';
import { BookService } from '../../services/book-service';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-show-book',
  imports: [AsyncPipe],
  templateUrl: './show-book.html',
  styleUrl: './show-book.css',
})
export class ShowBook {
  bookservice:BookService=inject(BookService);
}
