package org.northernarc.capexwithapi.dao;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.northernarc.capexwithapi.model.Capex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CapexDaoImpl implements CapexDao {

    Map<Integer, Capex> map;


    @PostConstruct
    public void init() {
        map = new HashMap<>();
        map.put(1, new Capex(1, "ford", "server hardware", 20, 5));
        map.put(2, new Capex(2, "ford", "Computers", 2, 5));
    }

    @Override
    public Capex save(Capex c1) {
        map.put(c1.getFinancialId(), c1);
        return c1;
    }

    @Override
    public Capex update(int id, Capex c1) {
        map.put(id, c1);
        return c1;
    }

    @Override
    public void deleteByFinancialId(int id) {
        map.remove(id);
    }

    @Override
    public void deleteAll() {
        map.clear();
    }

    @Override
    public Capex findById(int id) {
        return map.get(id);

    }

    @Override
    public Map<Integer, Capex> findAll() {
        return map;
    }

    @PreDestroy
    public void delete() {
        map.clear();
    }
}
