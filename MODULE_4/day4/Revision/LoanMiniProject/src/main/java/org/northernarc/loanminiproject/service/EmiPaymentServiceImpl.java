package org.northernarc.loanminiproject.service;

import org.northernarc.loanminiproject.dto.CustomerPaymentSummaryDto;
import org.northernarc.loanminiproject.dto.PaymentSummaryDto;
import org.northernarc.loanminiproject.model.EmiPayment;
import org.northernarc.loanminiproject.repository.EmiPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmiPaymentServiceImpl implements EmiPaymentService {

    @Autowired
    private EmiPaymentRepository emiPaymentRepository;

    @Override
    public EmiPayment findLatestPayment() {
        return emiPaymentRepository.findTopByOrderByPaymentDateDesc().orElse(null);
    }

    @Override
    public List<PaymentSummaryDto> getTotalEmiCollectionByCity() {
        return emiPaymentRepository.findTotalEmiCollectionByCity();
    }

    @Override
    public List<CustomerPaymentSummaryDto> getLatestPaymentByCustomer() {
        return emiPaymentRepository.findLatestPaymentByCustomer();
    }
}

