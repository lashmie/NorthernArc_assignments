package org.northernarc.applicationspringjdbc.service;

import org.northernarc.applicationspringjdbc.model.Capex;

import java.util.Map;

public interface CapexDaoService {
    Capex addCapex(Capex capex);
    Capex findCapexById(Long id);
    void deleteCapexById(Long id);
    Capex updateCapexDetails(Long id, Capex capex);
    Map<Long, Capex> showCapex();
    void clearAllCapex();
}

