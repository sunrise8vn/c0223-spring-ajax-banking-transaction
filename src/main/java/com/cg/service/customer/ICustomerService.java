package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.dto.*;
import com.cg.service.IGeneralService;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ICustomerService extends IGeneralService<Customer, Long> {

    List<Customer> findAllByIdNot(Long id);

    List<CustomerResDTO> findAllCustomerResDTOS();

    List<CustomerResDTO> findAllRecipientsWithoutSenderId(@Param("senderId") Long senderId);

    Boolean existsByEmail(String email);

    CustomerCreResDTO create(CustomerCreReqDTO customerCreReqDTO);

    Customer update(Customer customer, CustomerUpReqDTO customerUpReqDTO);

    Customer deposit(Deposit deposit);

    void transfer(TransferCreReqDTO transferCreReqDTO);

    void incrementBalance(Long id, BigDecimal amount);

    void transfer(Transfer transfer);
}
