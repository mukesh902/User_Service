package com.userApi.User_Registration_Module.repository;


import com.userApi.User_Registration_Module.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBankAccountRepository extends JpaRepository<BankAccount, Long> {

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByPanNumber(String panNumber);

    boolean existsByAadharNumber(String aadharNumber);
}
