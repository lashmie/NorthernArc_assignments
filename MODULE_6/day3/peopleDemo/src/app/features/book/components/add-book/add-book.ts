import { Component, inject } from '@angular/core';
import { BookService } from '../../services/book-service';
import is from '@angular/common/locales/extra/is';
import BookDTO from '../../dto/BookDTO';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-book',
  imports: [FormsModule],
  templateUrl: './add-book.html',
  styleUrl: './add-book.css',
})
export class AddBook {
  private title: string = '';
  private author: string = '';
  bookService: BookService = inject(BookService);

  protected book: BookDTO = {
  id: 0,
  title: '',
  author: ''
};
  addBook() {
  if (
    this.book.title.trim() &&
    this.book.author.trim()
  ) {
    this.bookService.addBook(this.book);

    this.book = {
      id: 0,
      title: '',
      author: ''
    };
  }
}

}
