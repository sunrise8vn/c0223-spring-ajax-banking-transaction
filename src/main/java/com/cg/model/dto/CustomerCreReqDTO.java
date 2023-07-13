package com.cg.model.dto;


import com.cg.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerCreReqDTO {

    private String fullName;
    private String email;
    private String phone;
    private LocationRegionCreReqDTO locationRegion;

    public Customer toCustomer() {
        return new Customer()
                .setId(null)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setBalance(BigDecimal.ZERO)
                .setLocationRegion(locationRegion.toLocationRegion())
                ;
    }

}
