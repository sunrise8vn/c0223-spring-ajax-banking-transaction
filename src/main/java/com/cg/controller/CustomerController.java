package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.service.customer.ICustomerService;
import com.cg.service.deposit.IDepositService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IDepositService depositService;

    @GetMapping
    public String showListPage(Model model) {
        List<Customer> customers = customerService.findAll();

        model.addAttribute("customers", customers);

        return "customer/list";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer/create";
    }

    @GetMapping("/deposit/{customerId}")
    public String showDepositPage(@PathVariable Long customerId, Model model) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "ID khách hàng không tồn tại");
        }
        else {
            Customer customer = customerOptional.get();
            Deposit deposit = new Deposit();
            deposit.setCustomer(customer);

            model.addAttribute("deposit", deposit);
        }

        return "customer/deposit";
    }

    @GetMapping("/transfer/{senderId}")
    public String showTransferPage(@PathVariable Long senderId, Model model) {

        Optional<Customer> senderOptional = customerService.findById(senderId);

        if (senderOptional.isEmpty()) {
            return "error/404";
        }

        Customer sender = senderOptional.get();

        Transfer transfer = new Transfer();
        transfer.setSender(sender);

        model.addAttribute("transfer", transfer);

        List<Customer> recipients = customerService.findAllByIdNot(senderId);

        model.addAttribute("recipients", recipients);

        return "customer/transfer";
    }


    @PostMapping("/create")
    public String doCreate(Model model, @ModelAttribute Customer customer, BindingResult bindingResult) {

        new Customer().validate(customer, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("hasError", true);
            return "customer/create";
        }

        String email = customer.getEmail();

        Boolean existsEmail = customerService.existsByEmail(email);

        if (existsEmail) {
            model.addAttribute("notValid", true);
            model.addAttribute("message", "Email đã tồn tại");
            return "customer/create";
        }

        customer.setId(null);
        customer.setBalance(BigDecimal.ZERO);
        customerService.save(customer);

        return "customer/create";
    }

    @PostMapping("/deposit/{customerId}")
    public String doDeposit(@ModelAttribute Deposit deposit, @PathVariable Long customerId, Model model) {
        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "ID khách hàng không tồn tại");
        }
        else {
            try {
                Customer customer = customerService.deposit(deposit);
                deposit.setCustomer(customer);

                model.addAttribute("deposit", deposit);
            }
            catch (Exception ex) {
                return "error/500";
            }
        }

        return "customer/deposit";
    }

    @PostMapping("/transfer/{senderId}")
    public String transfer(@PathVariable Long senderId, Model model, @ModelAttribute Transfer transfer, BindingResult bindingResult) {
        Optional<Customer> senderOptional = customerService.findById(senderId);

        if (senderOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "ID người gửi tiền không tồn tại");
            return "customer/transfer";
        }

        customerService.transfer(transfer);

        List<Customer> recipients = customerService.findAllByIdNot(senderId);

        Optional<Customer> newSender = customerService.findById(senderId);

        transfer = new Transfer();
        transfer.setSender(newSender.get());

        model.addAttribute("transfer", transfer);
        model.addAttribute("recipients", recipients);

        return "customer/transfer";
    }

}
