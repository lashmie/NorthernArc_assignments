import { Injectable, Service } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import BookDTO from '../dto/BookDTO';

@Injectable({
  providedIn: 'root',
})
export class BookService {
    //for book we are going to create array of object books(which contains id title and author )
    private books: BookDTO[] = [
        { id: 1, title: 'Book 1', author: 'Author 1' },
        { id: 2, title: 'Book 2', author: 'Author 2' }, 
        { id: 3, title: 'Book 3', author: 'Author 3' }
    ];
    private books$ = new BehaviorSubject<BookDTO[]>(this.books);

    getBooks() {
        return this.books$.asObservable();
    }

    addBook(book: BookDTO) {

    const newId =
      Math.max(...this.books.map(b => b.id), 0) + 1;

    const newBook: BookDTO = {
      ...book,
      id: newId
    };

    this.books = [...this.books, newBook];
    this.books$.next(this.books);
}

    removeBook(id: number) {
        // const currentBooks = this.books$.getValue();
        // this.books$.next(currentBooks.filter(b => b.id !== id));
        this.books = [...this.books].filter(b => b.id !== id);
        this.books$.next(this.books);
    }

    updateBook(id: number, updatedBook: BookDTO): void {
  this.books = this.books.map(book =>
    book.id === id
      ? { ...updatedBook, id: id }
      : book
  );

  this.books$.next([...this.books]);
}
}
