package demo.dao;


import demo.model.CapexFinance;

public interface CapexFinanceDao {

    void save(CapexFinance finance);

    void update(CapexFinance finance);

    void deleteById(int id);

    CapexFinance findById(int id);

    void findAll();

    void deleteAll();
}
