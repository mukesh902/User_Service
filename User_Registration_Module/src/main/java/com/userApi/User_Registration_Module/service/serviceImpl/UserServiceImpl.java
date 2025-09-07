package com.userApi.User_Registration_Module.service.serviceImpl;

import com.userApi.User_Registration_Module.dto.BankDetailDto;
import com.userApi.User_Registration_Module.dto.UserDto;
import com.userApi.User_Registration_Module.dto.UserWithBankDto;
import com.userApi.User_Registration_Module.entity.User;
import com.userApi.User_Registration_Module.entity.BankAccount;
import com.userApi.User_Registration_Module.repository.UserBankAccountRepository;
import com.userApi.User_Registration_Module.repository.UserRepository;
import com.userApi.User_Registration_Module.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserBankAccountRepository userBankAccountRepository;

    @Override
    public ResponseEntity<?> createNewUser(User user) {
        if(user.getFirstName() == null || user.getFirstName().isEmpty()) {
            return new ResponseEntity<>("First Name is mandatory", HttpStatus.BAD_REQUEST);
        }
        User save = userRepository.save(user);
        return new ResponseEntity<>("User saved with ID" + save.getId(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent())
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        return new ResponseEntity<>("No User present with ID: "+ id, HttpStatus.NOT_FOUND);
    }

    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll();

    }

    @Override
    public List<User> searchUsers(String userType, String city) {
        List<User> response = new ArrayList<>();
        // Approach 1
//        List<User> result = userRepository.findAll();
//        List<User> response = new ArrayList<>();
//        if(userType != null && city == null) {
//            response = result.stream().filter(x -> x.getUserType().equalsIgnoreCase(userType)).collect(Collectors.toList());
//        }
//        if(userType == null && city != null) {
//            response = result.stream().filter(x -> x.getCityName().equalsIgnoreCase(city)).collect(Collectors.toList());
//        }
//        if(userType != null && city != null) {
//            response = result.stream().filter(x -> x.getCityName().equalsIgnoreCase(city) && x.getUserType().equalsIgnoreCase(userType)).collect(Collectors.toList());
//        }
//        return response;

        //Approach 2
//        if(userType != null && city != null) {
//            response = userRepository.findByCityNameAndUserType(city, userType);
//        }
//        if(userType == null && city != null) {
//            response = userRepository.findByCityName(city);
//        }
//        if(userType != null && city == null) {
//            response = userRepository.findByUserType(userType);
//        }
//        return response;


        // Approach 3
         return userRepository.searchUsersWithFilters(city, userType);

         // Approach 4 - queryBuilder

    }

    @Override
    public ResponseEntity updateUserById(Long id, User user) {

        Optional<User> existingUserOptional = userRepository.findById(id);
        if(!existingUserOptional.isPresent())
            return new ResponseEntity<>("User not available with id :"+ id,HttpStatus.NO_CONTENT);

        User existingUser = existingUserOptional.get();
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setAge(user.getAge());
        existingUser.setEmail(user.getEmail());
        existingUser.setMobileNumber(user.getMobileNumber());
        existingUser.setCityName(user.getCityName());
        existingUser.setUserType(user.getUserType());

        User updatedUser = userRepository.save(existingUser);

        return ResponseEntity.ok(updatedUser);
    }

    //Bank Account existing user

    @Override
    public ResponseEntity<?> createAccountForExistingUser(Long userId, BankAccount userBankAccount) {
        Optional<User> existingUserOptional = userRepository.findById(userId);
        if(!existingUserOptional.isPresent())
            return new ResponseEntity<>("User not available with id :"+ userId, HttpStatus.NO_CONTENT);
        BankAccount saveAccount = userBankAccountRepository.save(userBankAccount);
        return new ResponseEntity<>("Bank account "+ saveAccount.getAccountNumber() +
                " is created for User" + saveAccount.getUserId()+" saved with ID" +
                saveAccount.getAccountID(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> getUserBankAccountByBankID(Long bankId) {
        if (bankId == null) {
            return new ResponseEntity<>("Bank ID is missing", HttpStatus.BAD_REQUEST);
        }
        Optional<BankAccount> bankDetails = userBankAccountRepository.findById(bankId);
        if(!bankDetails.isPresent()) {
            return new ResponseEntity<>("No Bank account present with ID: " + bankId, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(bankDetails);
    }

    @Override
    public ResponseEntity<?> getUserBankAccountByUserID(Long userId) {
        if (userId == null) {
            return new ResponseEntity<>("Bank ID is missing", HttpStatus.BAD_REQUEST);
        }
        Optional<BankAccount> bankDetails = userBankAccountRepository.findById(userId);
        if(!bankDetails.isPresent()) {
            return new ResponseEntity<>("No Bank account present with ID: " + userId, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(bankDetails);
    }

    @Override
    public List<BankAccount> getAllBankDetails() {
        List<BankAccount> bankAccounts = userBankAccountRepository.findAll();
        return bankAccounts;
    }

    @Override
    public List<User> getUserDetailsById(Long userId) {
        return userRepository.findById(userId).stream().toList();
    }

    @Override
    public List<BankAccount> getBankDetailsById(Long userId) {
        return userBankAccountRepository.findById(userId).stream().toList();
    }

    @Override
    public ResponseEntity<?> createFreshUserWithBankAccount(UserWithBankDto userWithBankDto) {
        // Extract DTOs
        UserDto userDto = userWithBankDto.getUserDto();
        BankDetailDto bankDetailDto = userWithBankDto.getBankDetailDto();

        // Validate bank details
        ResponseEntity<?> validationResponse = validateBankDetails(bankDetailDto);
        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            return validationResponse;
        }

        // Check if user and bank mobile numbers match
        if (!userDto.getMobileNumber().equals(bankDetailDto.getMobileNumber())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User mobile number does not match with Bank Account mobile number. Please enter the same mobile number.");
        }

        // Map and save user
        User user = mapToUserEntity(userDto);
        User savedUser = userRepository.save(user);

        // Map and save bank account
        BankAccount bankAccount = mapToBankEntity(bankDetailDto);
        bankAccount.setUserId(savedUser.getId().toString());
        BankAccount savedBankAccount = userBankAccountRepository.save(bankAccount);

        // Return success response
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User and Bank details are saved for "
                        + savedUser.getFirstName()
                        + " and Account number: " + savedBankAccount.getAccountNumber());
    }


    private User mapToUserEntity(UserDto userDto) {
        User user = new User();
        user.setUserType(userDto.getUserType());
        user.setAge(userDto.getAge());
        user.setEmail(userDto.getEmail());
        user.setCityName(userDto.getCityName());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setMobileNumber(userDto.getMobileNumber());
        return user;
    }

    private BankAccount mapToBankEntity(BankDetailDto bankDetailDto) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(bankDetailDto.getAccountNumber());
        bankAccount.setAccountType(bankDetailDto.getAccountType());
        bankAccount.setBankName(bankDetailDto.getBankName());
        bankAccount.setAadharNumber(bankDetailDto.getAadharNumber());
        bankAccount.setBrancCode(bankDetailDto.getBranchCode());
        bankAccount.setBalance(bankDetailDto.getBalance());
        bankAccount.setIfscCode(bankDetailDto.getIfscCode());
        bankAccount.setPanNumber(bankDetailDto.getPanNumber());
        bankAccount.setMobileNumber(bankDetailDto.getMobileNumber());
        return bankAccount;
    }

    public ResponseEntity<?> validateBankDetails(BankDetailDto dto) {

        if (userBankAccountRepository.existsByMobileNumber(dto.getMobileNumber())) {
            return new ResponseEntity<>("Mobile number already exists in the database.", HttpStatus.BAD_REQUEST);
        }

        if (userBankAccountRepository.existsByPanNumber(dto.getPanNumber())) {
            return new ResponseEntity<>("PAN number already exists in the database.", HttpStatus.BAD_REQUEST);
        }

        if (userBankAccountRepository.existsByAadharNumber(dto.getAadharNumber())) {
            return new ResponseEntity<>("Aadhar number already exists in the database.", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("New Bank details can add for the user");
    }

}
