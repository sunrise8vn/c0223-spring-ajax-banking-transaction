package com.cg.model;


import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "deposits")
public class Deposit extends BaseEntity implements Validator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    referencedColumnName: Tham chiếu tới cột ID của bảng customers
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @Column(precision = 10, scale = 0, nullable = false)
    private BigDecimal transactionAmount;

    public Deposit() {
    }

    public Deposit(Long id, Customer customer, BigDecimal transactionAmount) {
        this.id = id;
        this.customer = customer;
        this.transactionAmount = transactionAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Deposit.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Deposit deposit = (Deposit) o;

        BigDecimal transactionAmount = deposit.getTransactionAmount();
    }
}
