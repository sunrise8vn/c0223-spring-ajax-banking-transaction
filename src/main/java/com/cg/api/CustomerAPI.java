package com.cg.api;

import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.model.Customer;
import com.cg.model.dto.*;
import com.cg.service.customer.ICustomerService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/customers")
public class CustomerAPI {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ValidateUtils validateUtils;

    @GetMapping
    public ResponseEntity<?> getAllCustomers() {

//        List<Customer> customers = customerService.findAll();

        List<CustomerResDTO> customerResDTOS = customerService.findAllCustomerResDTOS();

//        for (Customer item : customers) {
//            CustomerResDTO customerResDTO = item.toCustomerResDTO();
//
//            customerResDTOS.add(customerResDTO);
//        }

        return new ResponseEntity<>(customerResDTOS, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getById(@PathVariable Long customerId) {

        Customer customer = customerService.findById(customerId).orElseThrow(() -> {
            throw new DataInputException("Mã khách hàng không tồn tại");
        });

        CustomerResDTO customerResDTO = customer.toCustomerResDTO();

        return new ResponseEntity<>(customerResDTO, HttpStatus.OK);
    }

    @GetMapping("/recipients-without-sender/{senderId}")
    public ResponseEntity<?> getAllRecipientsWithoutSender(@PathVariable Long senderId) {

        List<CustomerResDTO> recipients = customerService.findAllRecipientsWithoutSenderId(senderId);

        return new ResponseEntity<>(recipients, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerCreReqDTO customerCreReqDTO) {

        String email = customerCreReqDTO.getEmail().trim();

        Boolean emailExists = customerService.existsByEmail(email);

        if (emailExists) {
            throw new EmailExistsException("Email đã tồn tại");
        }

        CustomerCreResDTO customerCreResDTO = customerService.create(customerCreReqDTO);

        return new ResponseEntity<>(customerCreResDTO, HttpStatus.CREATED);
    }


    @PatchMapping("/{customerId}")
    public ResponseEntity<?> update(@PathVariable("customerId") String customerIdStr, @Validated @RequestBody CustomerUpReqDTO customerUpReqDTO, BindingResult bindingResult) {

        if (!validateUtils.isNumberValid(customerIdStr)) {
            throw new DataInputException("Mã khách hàng không hợp lệ");
        }

        Long customerId = Long.parseLong(customerIdStr);

        Customer customer = customerService.findById(customerId).orElseThrow(() -> {
           throw new DataInputException("Mã khách hàng không tồn tại");
        });

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Customer customerUpdate = customerService.update(customer, customerUpReqDTO);

        CustomerUpResDTO customerUpResDTO = customerUpdate.toCustomerUpResDTO();

        return new ResponseEntity<>(customerUpResDTO, HttpStatus.OK);
    }
}
