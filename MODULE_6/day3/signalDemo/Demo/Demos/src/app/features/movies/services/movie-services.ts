import { Injectable, Service, signal, WritableSignal, } from '@angular/core';
import movieDTO from '../dto/movieDTO';


@Injectable({
    providedIn: 'root'
    })
export class MovieServices {
    //we are going to create the array of movies over here using signals and we are going to create the methods to add, remove and update the movies over here...
    protected movies:WritableSignal<movieDTO[]> = signal([
        { id: 1, title: 'The Shawshank Redemption', director: 'Frank Darabont', genre: 'Drama', rating: 9.3 },
        { id: 2, title: 'The Godfather', director: 'Francis Ford Coppola', genre: 'Crime', rating: 9.2 },
        { id: 3, title: 'The Dark Knight', director: 'Christopher Nolan', genre: 'Action', rating: 9.0 },
        { id: 4, title: 'Pulp Fiction', director: 'Quentin Tarantino', genre: 'Crime', rating: 8.9 },
        { id: 5, title: 'Forrest Gump', director: 'Robert Zemeckis', genre: 'Drama', rating: 8.8 }
    ]);
    //get the movies
    getMovies():WritableSignal<movieDTO[]> {
        return this.movies;
    }
    //add the movie
    addMovie(movie:movieDTO) {
        this.movies.set([...this.movies(), movie]);
    }
    //remove the movie
    removeMovie(id:number):void {
        this.movies.set(this.movies().filter(m => m.id !== id));
    }
    //update the movie
    updateMovie(id:number, updatedMovie:movieDTO):void {
        this.movies.set(this.movies().map(m => m.id === id ? updatedMovie : m));
    }
  
}


