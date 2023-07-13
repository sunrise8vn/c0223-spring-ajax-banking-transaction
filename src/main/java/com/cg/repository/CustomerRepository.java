package com.cg.repository;

import com.cg.model.Customer;
import com.cg.model.dto.CustomerResDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    List<Customer> findAllByIdNot(Long id);


    @Query("SELECT NEW com.cg.model.dto.CustomerResDTO (" +
                "cus.id, " +
                "cus.fullName, " +
                "cus.email," +
                "cus.phone," +
                "cus.balance, " +
                "cus.locationRegion" +
            ") " +
            "FROM Customer AS cus " +
            "WHERE cus.deleted = false "
    )
    List<CustomerResDTO> findAllCustomerResDTOS();


    @Query("SELECT NEW com.cg.model.dto.CustomerResDTO (" +
                "cus.id, " +
                "cus.fullName, " +
                "cus.email," +
                "cus.phone," +
                "cus.balance, " +
                "cus.locationRegion" +
            ") " +
            "FROM Customer AS cus " +
            "WHERE cus.deleted = false " +
            "AND cus.id != :senderId "
    )
    List<CustomerResDTO> findAllRecipientsWithoutSenderId(@Param("senderId") Long senderId);

    Boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE Customer AS cus SET cus.balance = cus.balance + :amount WHERE cus.id = :id")
    void incrementBalance(@Param("id") Long id, @Param("amount") BigDecimal amount);


    @Modifying
    @Query("UPDATE Customer AS cus SET cus.balance = cus.balance - :amount WHERE cus.id = :id")
    void decrementBalance(@Param("id") Long id, @Param("amount") BigDecimal amount);

}
