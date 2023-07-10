package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;

public interface ICustomerService extends IGeneralService<Customer, Long> {

    List<Customer> findAllByIdNot(Long id);

    Boolean existsByEmail(String email);

    Customer deposit(Deposit deposit);

    void incrementBalance(Long id, BigDecimal amount);

    void transfer(Transfer transfer);
}
