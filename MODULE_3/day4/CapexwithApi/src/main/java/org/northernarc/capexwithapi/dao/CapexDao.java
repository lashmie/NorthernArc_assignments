package org.northernarc.capexwithapi.dao;

import org.northernarc.capexwithapi.model.Capex;

import java.util.Map;

public interface CapexDao {
    Capex save(Capex c1);
    Capex update(int id,Capex c1);
    void deleteByFinancialId(int id);
    void deleteAll();
    Capex findById(int id);
    Map<Integer,Capex>  findAll();
}
