package org.springboot.authapi.Dto;
import lombok.Data;


@Data
public class RegisterUserDto {
    private String email;
    private String password;
    private String fullName;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }
}

//--------------------------------------------8---------------------------------