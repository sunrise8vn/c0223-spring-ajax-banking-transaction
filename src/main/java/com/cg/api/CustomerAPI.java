package com.cg.api;

import com.cg.model.Customer;
import com.cg.model.dto.CustomerResDTO;
import com.cg.service.customer.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


@RestController
@RequestMapping("/api/customers")
public class CustomerAPI {

    @Autowired
    private ICustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getAllCustomers() {

        List<Customer> customers = customerService.findAll();

        List<CustomerResDTO> customerResDTOS = new ArrayList<>();

        for (Customer item : customers) {
            CustomerResDTO customerResDTO = new CustomerResDTO();
            customerResDTO.setId(item.getId());
            customerResDTO.setFullName(item.getFullName());
            customerResDTO.setEmail(item.getEmail());
            customerResDTO.setPhone(item.getPhone());
            customerResDTO.setBalance(item.getBalance());
            customerResDTO.setAddress(item.getAddress());

            customerResDTOS.add(customerResDTO);
        }

        return new ResponseEntity<>(customerResDTOS, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getById(@PathVariable Long customerId) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("message", "Long ko ton tai");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(customerOptional.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Customer customer) {

        customer.setId(null);
        customer.setBalance(BigDecimal.ZERO);
        Customer newCustomer = customerService.save(customer);

        return new ResponseEntity<>(newCustomer, HttpStatus.OK);
    }
}
