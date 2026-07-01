package org.northernarc.loanminiproject.service;

import org.northernarc.loanminiproject.model.EmiSchedule;
import java.util.List;

public interface EmiScheduleService {
    List<EmiSchedule> findHighestOverdueAmount();
}

