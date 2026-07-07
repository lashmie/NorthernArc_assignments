package com.northernarc.capex.nbfc.dao;


import com.northernarc.capex.nbfc.model.CapexFinance;

import java.util.List;

public interface CapexFinanceDao {

    void save(CapexFinance finance);

    void update(CapexFinance finance);

    CapexFinance findById(int financeId);

    List<CapexFinance> findAll();

    void deleteById(int financeId);

    void deleteAll();
}
