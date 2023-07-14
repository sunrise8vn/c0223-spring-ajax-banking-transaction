package com.cg.model.dto;

import com.cg.model.Customer;
import com.cg.model.LocationRegion;
import lombok.*;

import javax.validation.Valid;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerUpReqDTO {

    private String fullName;
    private String email;
    private String phone;

    @Valid
    private LocationRegionUpReqDTO locationRegion;

    public Customer toCustomer(Long customerId, LocationRegion locationRegion) {
        return new Customer()
                .setId(customerId)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setLocationRegion(locationRegion)
                ;
    }
}
