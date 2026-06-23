package com.northernarc.capex.nbfc.dao;

import com.northernarc.capex.nbfc.model.CapexFinance;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CapexFinanceDaoImpl implements CapexFinanceDao {

    private Map<Integer, CapexFinance> capexMap = new HashMap<>();
    @PostConstruct
    public void init() {

        CapexFinance f1 = new CapexFinance(
                1,
                "ABC Manufacturing",
                "CNC Machine",
                5000000,
                60,
                true
        );

        CapexFinance f2 = new CapexFinance(
                2,
                "XYZ Industries",
                "Robotic Arm",
                8000000,
                48,
                false
        );

        capexMap.put(1, f1);
        capexMap.put(2, f2);

        System.out.println("Initial CAPEX data loaded using @PostConstruct");
    }

    @Override
    public void save(CapexFinance finance) {

        capexMap.put(finance.getFinanceId(), finance);

        System.out.println("CAPEX Finance saved successfully.");
    }

    @Override
    public void update(CapexFinance finance) {

        if (capexMap.containsKey(finance.getFinanceId())) {

            capexMap.put(finance.getFinanceId(), finance);

            System.out.println("CAPEX Finance updated successfully.");
        } else {

            System.out.println("Finance ID not found.");
        }
    }

    @Override
    public CapexFinance findById(int financeId) {

        return capexMap.get(financeId);
    }

    @Override
    public List<CapexFinance> findAll() {

        return new ArrayList<>(capexMap.values());
    }

    @Override
    public void deleteById(int financeId) {

        if (capexMap.remove(financeId) != null) {

            System.out.println("Record deleted successfully.");
        } else {

            System.out.println("Finance ID not found.");
        }
    }

    @Override
    public void deleteAll() {

        capexMap.clear();

        System.out.println("All CAPEX records deleted.");
    }
    @PreDestroy
    public void destroy() {
        System.out.println("CapexFinanceDaoImpl bean is being destroyed - clearing data");
        capexMap.clear();
    }
}