package com.cg.service.customer;

import com.cg.exception.DataInputException;
import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.LocationRegion;
import com.cg.model.Transfer;
import com.cg.model.dto.*;
import com.cg.repository.CustomerRepository;
import com.cg.repository.DepositRepository;
import com.cg.repository.LocationRegionRepository;
import com.cg.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private LocationRegionRepository locationRegionRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> findAllByIdNot(Long id) {
        return customerRepository.findAllByIdNot(id);
    }

    @Override
    public List<CustomerResDTO> findAllCustomerResDTOS() {
        return customerRepository.findAllCustomerResDTOS();
    }

    @Override
    public List<CustomerResDTO> findAllRecipientsWithoutSenderId(Long senderId) {
        return customerRepository.findAllRecipientsWithoutSenderId(senderId);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public CustomerCreResDTO create(CustomerCreReqDTO customerCreReqDTO) {
        LocationRegionCreReqDTO locationRegionCreReqDTO = customerCreReqDTO.getLocationRegion();
        LocationRegion locationRegion = locationRegionCreReqDTO.toLocationRegion();
        locationRegionRepository.save(locationRegion);

        Customer customer = customerCreReqDTO.toCustomer();
        customer.setLocationRegion(locationRegion);
        customerRepository.save(customer);

        CustomerCreResDTO customerCreResDTO = customer.toCustomerCreResDTO();

        return customerCreResDTO;
    }

    @Override
    public Customer update(Customer customer, CustomerUpReqDTO customerUpReqDTO) {
        LocationRegionUpReqDTO locationRegionUpReqDTO = customerUpReqDTO.getLocationRegion();
        LocationRegion locationRegion = locationRegionUpReqDTO.toLocationRegion();
        locationRegionRepository.save(locationRegion);

        Customer customerUpdate = customerUpReqDTO.toCustomer(customer.getId(), locationRegion);
        customerUpdate.setBalance(customer.getBalance());
        customerRepository.save(customerUpdate);

        return customerUpdate;
    }

    @Override
    public Customer deposit(Deposit deposit) {
        deposit.setId(null);
        depositRepository.save(deposit);

        Customer customer = deposit.getCustomer();
        BigDecimal transactionAmount = deposit.getTransactionAmount();

        customerRepository.incrementBalance(customer.getId(), transactionAmount);

        BigDecimal newBalance = customer.getBalance().add(transactionAmount);
        customer.setBalance(newBalance);

        return customer;
    }

    @Override
    public void transfer(TransferCreReqDTO transferCreReqDTO) {
        Long senderId = transferCreReqDTO.getSenderId();
        Long recipientId = transferCreReqDTO.getRecipientId();

        Customer sender = customerRepository.findById(senderId).orElseThrow(() -> {
           throw new DataInputException("Mã người gửi không tồn tại");
        });

        Customer recipient = customerRepository.findById(recipientId).orElseThrow(() -> {
           throw new DataInputException("Mã người nhận không tồn tại");
        });

        BigDecimal transferAmount = transferCreReqDTO.getTransferAmount();
        long fees = 10L;
        BigDecimal feesAmount = transferAmount.multiply(BigDecimal.valueOf(fees)).divide(BigDecimal.valueOf(100L));
        BigDecimal transactionAmount = transferAmount.add(feesAmount);

        customerRepository.decrementBalance(senderId, transactionAmount);
        customerRepository.incrementBalance(recipientId, transferAmount);

        Transfer transfer = new Transfer();
        transfer.setSender(sender);
        transfer.setRecipient(recipient);
        transfer.setTransferAmount(transferAmount);
        transfer.setFees(fees);
        transfer.setFeesAmount(feesAmount);
        transfer.setTransactionAmount(transactionAmount);
        transferRepository.save(transfer);

    }

    @Override
    public void incrementBalance(Long id, BigDecimal amount) {
        customerRepository.incrementBalance(id, amount);
    }

    @Override
    public void transfer(Transfer transfer) {
        Customer sender = transfer.getSender();
        Customer recipient = transfer.getRecipient();

        BigDecimal transferAmount = transfer.getTransferAmount();
        long fees = 10L;
        BigDecimal feesAmount = transferAmount.multiply(BigDecimal.valueOf(fees)).divide(BigDecimal.valueOf(100L));
        BigDecimal transactionAmount = transferAmount.add(feesAmount);

        transfer.setFees(fees);
        transfer.setFeesAmount(feesAmount);
        transfer.setTransactionAmount(transactionAmount);

        customerRepository.decrementBalance(sender.getId(), transactionAmount);

        customerRepository.incrementBalance(recipient.getId(), transferAmount);

        transferRepository.save(transfer);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }
}
