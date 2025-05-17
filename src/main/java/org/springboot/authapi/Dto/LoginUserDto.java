package org.springboot.authapi.Dto;

import lombok.Data;

@Data
public class  LoginUserDto {
    private String email;

    private String password;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

}

//---------------------------------------7--------------------------------