package com.northernarc.firstWebApplication.dao;

import com.northernarc.firstWebApplication.model.Movie;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MovieDaoImpl implements MovieDao {
    Map<Integer, Movie> map = new HashMap<>();

    @PostConstruct
    public void init() {
        map.put(1, new Movie(1, "karma", "lavanya", 2026, "life", 9));
    }

    @Override
    public void save(Movie movie) {
        map.put(movie.getId(), movie);
    }

    @Override
    public Movie findById(int id) {
        return map.get(id);
    }

    @Override
    public Map<Integer,Movie> findall() {
        return map;
    }
    @Override
    public void update(int id, Movie movie) {
        if (map.containsKey(id)) {
            map.put(id, movie);
        }
    }
    @Override
    public void deleteById(int id) {
        map.remove(id);

    }
    @Override
    public void deleteAll(){
        map.clear();
    }
    @PreDestroy
    public void destroy() {
        map.clear();
    }
}
