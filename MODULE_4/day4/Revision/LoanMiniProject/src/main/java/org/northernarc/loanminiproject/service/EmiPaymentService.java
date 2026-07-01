package org.northernarc.loanminiproject.service;

import org.northernarc.loanminiproject.dto.CustomerPaymentSummaryDto;
import org.northernarc.loanminiproject.dto.PaymentSummaryDto;
import org.northernarc.loanminiproject.model.EmiPayment;

import java.util.List;

public interface EmiPaymentService {
    EmiPayment findLatestPayment();
    List<PaymentSummaryDto> getTotalEmiCollectionByCity();
    List<CustomerPaymentSummaryDto> getLatestPaymentByCustomer();
}

