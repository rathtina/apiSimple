package org.springboot.authapi.ResponseEnities;
import lombok.Data;


@Data
public class LoginResponse {
    private String token;

    private String fullName;


    public String getToken() {
        return token;
    }

    public String getFullName() {
        return fullName;
    }

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;  // Allows method chaining
    }

    public LoginResponse setFullName(String fullName) {
        this.fullName = fullName;
        return this;  // Allows method chaining
    }



}

//--------------------------------------------------------9---------------------------------------------
