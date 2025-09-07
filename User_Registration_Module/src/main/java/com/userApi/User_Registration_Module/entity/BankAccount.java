package com.userApi.User_Registration_Module.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "my_db")
public class BankAccount {

    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountID;

    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "account_type")
    private String accountType;
    @Column(name = "balance")
    private String balance;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "ifsc_code")
    private String ifscCode;
    @Column(name = "branch_code")
    private String branchCode;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "aadhar_number")
    private String aadharNumber;
    @Column(name = "pan_number")
    private String panNumber;
    @Column(name = "mobile_number")
    private String mobileNumber;


    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBrancCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }
}
