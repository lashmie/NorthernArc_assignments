package org.northernarc.applicationspringjdbc.service;

import org.northernarc.applicationspringjdbc.model.Capex;
import org.northernarc.applicationspringjdbc.repository.CapexDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CapexDaoServiceImpl implements CapexDaoService {

    @Autowired
    public CapexDao capexDao;

    @Override
    public Capex addCapex(Capex capex) {
        return capexDao.save(capex);
    }

    @Override
    public Capex findCapexById(Long id) {
        return capexDao.findById(id).orElseThrow();
    }

    @Override
    public void deleteCapexById(Long id) {
        capexDao.deleteById(id);
    }

    @Override
    public Capex updateCapexDetails(Long id, Capex capex) {
        Capex existing = capexDao.findById(id).orElseThrow();
        existing.setCompanyName(capex.getCompanyName());
        existing.setAssetType(capex.getAssetType());
        existing.setTenure(capex.getTenure());
        existing.setAmount(capex.getAmount());
        existing.setVendor(capex.getVendor());
        existing.setStatus(capex.getStatus());
        return capexDao.save(existing);
    }

    @Override
    public Map<Long, Capex> showCapex() {
        return capexDao.findAll().stream()
                .collect(Collectors.toMap(Capex::getFinancialId, capex -> capex));
    }

    @Override
    public void clearAllCapex() {
        capexDao.deleteAll();
    }
}

