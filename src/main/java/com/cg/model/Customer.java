package com.cg.model;

import com.cg.model.dto.CustomerCreResDTO;
import com.cg.model.dto.CustomerResDTO;
import com.cg.model.dto.CustomerUpResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customers")
@Accessors(chain = true)
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

    @OneToOne
    @JoinColumn(name = "location_reigon_id", referencedColumnName = "id", nullable = false)
    private LocationRegion locationRegion;

    @Column(precision = 10, scale = 0, nullable = false, updatable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private Set<Deposit> deposits;


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


    public CustomerResDTO toCustomerResDTO() {
        return new CustomerResDTO()
            .setId(id)
            .setFullName(fullName)
            .setEmail(email)
            .setPhone(phone)
            .setBalance(balance)
            .setLocationRegion(locationRegion.toLocationRegionDTO())
            ;
    }

    public CustomerCreResDTO toCustomerCreResDTO() {
        return new CustomerCreResDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setBalance(balance)
                .setLocationRegion(locationRegion.toLocationRegionCreResDTO())
                ;
    }


    public CustomerUpResDTO toCustomerUpResDTO() {
        return new CustomerUpResDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setBalance(balance)
                .setLocationRegion(locationRegion.toLocationRegionUpResDTO())
                ;
    }
}
