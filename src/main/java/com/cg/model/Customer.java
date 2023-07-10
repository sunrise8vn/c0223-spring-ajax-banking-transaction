package com.cg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity implements Validator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotBlank(message = "Họ tên là bắt buộc")
    @Column(name = "full_name", nullable = false)
    private String fullName;

//    @NotBlank(message = "Email là bắt buộc")
    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private String address;

    @Column(precision = 10, scale = 0, nullable = false, updatable = false)
    private BigDecimal balance;


    @Override
    public boolean supports(Class<?> aClass) {
        return Customer.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Customer customer = (Customer) target;

        String fullName = customer.fullName;

        if (fullName.length() == 0) {
            errors.rejectValue("fullName", "fullName.empty" );
        }
        else {
            if (fullName.length() < 5 || fullName.length() > 20) {
                errors.rejectValue("fullName", "fullName.length");
            }
        }

    }
}
