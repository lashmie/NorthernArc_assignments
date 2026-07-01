package org.northernarc.loanminiproject.service;

import org.northernarc.loanminiproject.model.EmiSchedule;
import org.northernarc.loanminiproject.repository.EmiScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmiScheduleServiceImpl implements EmiScheduleService {

    @Autowired
    private EmiScheduleRepository emiScheduleRepository;

    @Override
    public List<EmiSchedule> findHighestOverdueAmount() {
        return emiScheduleRepository.findHighestOverdueAmount();
    }
}

