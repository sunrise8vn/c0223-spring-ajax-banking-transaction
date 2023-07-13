package com.cg.api;


import com.cg.exception.DataInputException;
import com.cg.model.Customer;
import com.cg.model.Transfer;
import com.cg.model.dto.CustomerResDTO;
import com.cg.model.dto.TransferCreReqDTO;
import com.cg.model.dto.TransferCreResDTO;
import com.cg.service.customer.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/transfers")
public class TransferAPI {

    @Autowired
    private ICustomerService customerService;


    @PostMapping
    public ResponseEntity<?> transfer(@RequestBody TransferCreReqDTO transferCreReqDTO) {

        Long senderId = transferCreReqDTO.getSenderId();
        Long recipientId = transferCreReqDTO.getRecipientId();

        customerService.transfer(transferCreReqDTO);

        TransferCreResDTO transferCreResDTO = new TransferCreResDTO();
        Customer sender = customerService.findById(senderId).get();
        Customer recipient = customerService.findById(recipientId).get();

        transferCreResDTO.setSender(sender.toCustomerResDTO());
        transferCreResDTO.setRecipient(recipient.toCustomerResDTO());

//        Map<String, CustomerResDTO> data = new HashMap<>();
//        data.put("sender", sender.toCustomerResDTO());
//        data.put("recipient", recipient.toCustomerResDTO());

        return new ResponseEntity<>(transferCreResDTO, HttpStatus.OK);
    }
}
