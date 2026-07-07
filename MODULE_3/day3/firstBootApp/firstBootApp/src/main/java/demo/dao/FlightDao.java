package demo.dao;

import demo.model.Flight;

public interface FlightDao{
    public void save(Flight flight);
    public void update(Flight flight);
    public  void deleteByNumber(int number);
    public void deleteAll();
    public Flight findByNumber(int number);
    public void findAll();
}
