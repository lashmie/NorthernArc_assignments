package com.northernarc.firstWebApplication.dao;

import com.northernarc.firstWebApplication.model.Movie;

import java.util.Collection;
import java.util.Map;

public interface MovieDao {
    public void save(Movie movie);
    public Movie findById(int id);
    public Map<Integer,Movie> findall();
    public void update(int id,Movie movie);
    public void deleteById(int id);
    public void deleteAll();
}
