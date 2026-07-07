package com.northernarc.firstWebApplication.controller;

import com.northernarc.firstWebApplication.dao.MovieDao;
import com.northernarc.firstWebApplication.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")

public class MovieController {
   @Autowired
    MovieDao movies;

   @RequestMapping("/{id}")
    public Movie getmovieByid(@PathVariable int id){
       return movies.findById(id);
   }
   @RequestMapping("")
    public Map<Integer,Movie> getAllMovies (){
       return movies.findall();
   }
   @RequestMapping("/delete/{id}")
    public void delete(@PathVariable int id){
       movies.deleteById(id);
   }
    @RequestMapping("/save/{id}/{title}/{director}/{year}/{genre}/{rating}")
    public void save(
            @PathVariable int id,
            @PathVariable String title,
            @PathVariable String director,
            @PathVariable int year,
            @PathVariable String genre,
            @PathVariable double rating
    ) {
        Movie movie = new Movie(id, title, director, year, genre, rating);
        movies.save(movie);
    }
    @RequestMapping("/update/{id}/{title}/{director}/{year}/{genre}/{rating}")
    public void update(
            @PathVariable int id,
            @PathVariable  String title,
            @PathVariable String director,
            @PathVariable int year,
            @PathVariable String genre,
            @PathVariable double rating
    ){
       Movie movie = new Movie(id,title,director,year,genre,rating);
       movies.update(id,movie);
    }
    @RequestMapping(value = "/deleteAll",method = {RequestMethod.DELETE})
    public String deleteall(){
       movies.deleteAll();
      return "all deleted......";
    }
}
