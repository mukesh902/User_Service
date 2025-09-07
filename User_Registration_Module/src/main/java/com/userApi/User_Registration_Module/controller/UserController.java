package com.userApi.User_Registration_Module.controller;


import com.userApi.User_Registration_Module.dto.UserWithBankDto;
import com.userApi.User_Registration_Module.entity.User;
import com.userApi.User_Registration_Module.entity.BankAccount;
import com.userApi.User_Registration_Module.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/create")
    private ResponseEntity<?> createUser(@RequestBody User user) {
        return userService.createNewUser(user);
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getUserByID(@PathVariable("id") Long id) {
        ResponseEntity<?> userById = userService.getUserById(id);
        return userById;
    }

    @GetMapping
    private ResponseEntity<?> findAllUser() {
        List<User> allUsers = userService.getAllUsers();
        if(allUsers.isEmpty())
            return new ResponseEntity<>("No users is present in the list", HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/search")
    private ResponseEntity<?> searchUser(@RequestParam(required = false) String userType, @RequestParam(required = false) String city) {
        if((userType == null || userType.isEmpty()) && (city == null || city.isEmpty())){
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        }
        List<User> users = userService.searchUsers(userType, city);
        return new ResponseEntity<>(users, HttpStatus.OK);

    }

    @PutMapping("/update/{id}")
    private ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody User user ){
        return userService.updateUserById(id, user);

    }

    // Bank Account
    // Create Bank account for existing users
    @PostMapping("/{userId}/create/account")
    private ResponseEntity<?> createAccountForExistingUser(@PathVariable("userId") Long userId, @RequestBody BankAccount userBankAccount){
        if(userId==null) {
            return new ResponseEntity<>("User ID is missing", HttpStatus.BAD_REQUEST);
        }
        return userService.createAccountForExistingUser(userId, userBankAccount);
    }

    //get account details for existing users
    //by bank Account ID
    @GetMapping("/bank/{bankId}/account-details")
    private ResponseEntity<?> getUserBankAccountByBankID(@PathVariable("bankId") Long bankId) {
        return userService.getUserBankAccountByBankID(bankId);
    }

    //by User Account ID
    @GetMapping("/bank/account-details/{userId}")
    private ResponseEntity<?> getUserBankAccountByUserID(@PathVariable("userId") Long userId) {
        return userService.getUserBankAccountByUserID(userId);
    }

    @GetMapping("all/bank-details")
    private ResponseEntity<?> getAllBankDetails() {
        List<BankAccount> allBankDetails = userService.getAllBankDetails(); // This should call a service method to fetch all bank details
        if(allBankDetails.isEmpty())
            return new ResponseEntity<>("No Bank details is present in the list", HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(allBankDetails);
    }

    //fetch bank details with user details by user ID
    @GetMapping("/{userId}/bank-user-details")
    private ResponseEntity<?> getBankAndUserDetailsById(@PathVariable("userId") Long userId) {
        List<User> userDetails= userService.getUserDetailsById(userId);
        if(userDetails.isEmpty())
            return new ResponseEntity<>("No User is present with ID: " + userId, HttpStatus.NOT_FOUND);
        List<BankAccount> userBankDetails =  userService.getBankDetailsById(userId);
        if(userBankDetails.isEmpty())
            return new ResponseEntity<>("No Bank details is present for User ID: " + userId, HttpStatus.NOT_FOUND);
        // Create a response object that contains both user and bank details
        return ResponseEntity.ok(new Object() {
            public List<User> user = userDetails;
            public List<BankAccount> bankDetails = userBankDetails;
        });
    }

    //FreshUserWithBankAccount

    @PostMapping("/create-with-account")
    private ResponseEntity<?> createFreshUserWithBankAccount(@RequestBody UserWithBankDto userWithBankDto) {
        if(userWithBankDto.getUserDto() == null || userWithBankDto.getBankDetailDto() == null)
            return new ResponseEntity<>("User or Bank account details is not available", HttpStatus.BAD_REQUEST);
        return userService.createFreshUserWithBankAccount(userWithBankDto);
    }
}
