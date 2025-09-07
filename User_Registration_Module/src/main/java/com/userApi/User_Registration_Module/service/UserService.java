package com.userApi.User_Registration_Module.service;


import com.userApi.User_Registration_Module.dto.UserWithBankDto;
import com.userApi.User_Registration_Module.entity.User;
import com.userApi.User_Registration_Module.entity.BankAccount;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface UserService {

    ResponseEntity<?> createNewUser(User user);

    ResponseEntity<?> getUserById(Long id);

    List<User> getAllUsers();

    List<User> searchUsers(String userType, String city);

    ResponseEntity updateUserById(Long id, User user);

    ResponseEntity<?> createAccountForExistingUser(Long userId, BankAccount userBankAccount);

    ResponseEntity<?> getUserBankAccountByBankID(Long bankId);

    ResponseEntity<?> getUserBankAccountByUserID(Long userId);

    List<BankAccount> getAllBankDetails();

    List<User> getUserDetailsById(Long userId);

    List<BankAccount> getBankDetailsById(Long userId);

    ResponseEntity<?> createFreshUserWithBankAccount(UserWithBankDto userWithBankDto);
}
