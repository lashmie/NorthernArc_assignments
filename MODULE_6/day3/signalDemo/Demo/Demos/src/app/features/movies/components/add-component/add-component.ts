import { Component, inject } from '@angular/core';
import { MovieServices } from '../../services/movie-services';
import movieDTO from '../../dto/movieDTO';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-component',
  imports: [FormsModule],
  templateUrl: './add-component.html',
  styleUrl: './add-component.css',
})
export class AddComponent {

  movieservices: MovieServices = inject(MovieServices);
//we are getting the movie name from the input field and passing it to the addMovie method of the MovieServices class
newMovie: movieDTO = {
  id: 0,
  title: '',
  director: '',
  genre: '',
  rating: 0
};
addMovie() {

  const movies = this.movieservices.getMovies()();

  const nextId = movies.length > 0
      ? Math.max(...movies.map(m => m.id)) + 1
      : 1;

  const movie: movieDTO = {
    id: nextId,
    title: this.newMovie.title,
    director: this.newMovie.director,
    genre: this.newMovie.genre,
    rating: this.newMovie.rating
  };

  this.movieservices.addMovie(movie);
}

}
