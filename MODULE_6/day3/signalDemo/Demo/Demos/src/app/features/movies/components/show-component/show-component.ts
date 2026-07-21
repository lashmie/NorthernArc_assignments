import { Component, inject } from '@angular/core';
import { MovieServices } from '../../services/movie-services';

@Component({
  selector: 'app-show-component',
  imports: [],
  templateUrl: './show-component.html',
  styleUrl: './show-component.css',
})
export class ShowComponent {
  movieservices: MovieServices = inject(MovieServices);
  movies = this.movieservices.getMovies();
}
