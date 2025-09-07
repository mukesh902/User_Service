package com.userApi.User_Registration_Module.dto;

public class UserWithBankDto {

    private UserDto userDto;
    private BankDetailDto bankDetailDto;

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public BankDetailDto getBankDetailDto() {
        return bankDetailDto;
    }

    public void setBankDetailDto(BankDetailDto bankDetailDto) {
        this.bankDetailDto = bankDetailDto;
    }
}
