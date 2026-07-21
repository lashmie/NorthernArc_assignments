import { Component, inject } from '@angular/core';
import { BookService } from '../../services/book-service';
import { FormsModule } from '@angular/forms';
import BookDTO from '../../dto/BookDTO';

@Component({
  selector: 'app-update-book',
  imports: [FormsModule],
  templateUrl: './update-book.html',
  styleUrl: './update-book.css',
})
export class UpdateBook {
  
  bookService: BookService = inject(BookService);
  protected id:number=0;
  //clear the title and author after updating the book
  protected book: BookDTO = {
    id: 0,
    title: '',
    author: ''
  };  
  updateBook() {
  if (
    this.book.id > 0 &&
    this.book.title.trim() &&
    this.book.author.trim()
  ) {
    this.bookService.updateBook(this.id, this.book);

    this.id = 0;
    this.book = {
      id: 0,
      title: '',
      author: ''
    };
  }
}
}



