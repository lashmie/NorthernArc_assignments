import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MovieServices } from '../../services/movie-services';


export interface movieDTO {
  id: number;
  title: string;
  director: string;
  genre: string;
  rating: number;
}

@Component({
  selector: 'app-update-movie',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './update-component.html'
})
export class UpdateComponent {

  movieServices = inject(MovieServices);

  movieDTO: movieDTO = {
    id: 0,
    title: '',
    director: '',
    genre: '',
    rating: 0
  };

  updateMovie(): void {

    const updatedMovie: movieDTO = {
      id: this.movieDTO.id,
      title: this.movieDTO.title,
      director: this.movieDTO.director,
      genre: this.movieDTO.genre,
      rating: this.movieDTO.rating
    };

    this.movieServices.updateMovie(
      updatedMovie.id,
      updatedMovie
    );

    this.movieDTO = {
      id: 0,
      title: '',
      director: '',
      genre: '',
      rating: 0
    };
  }
}