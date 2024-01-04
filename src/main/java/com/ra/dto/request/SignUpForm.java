package com.ra.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {
    @NotBlank(message = "Name must not be blank")
    @Size(min = 8,max = 12,message = "Name must be between 8 and 12 characters")
    private String name;
    @NotBlank(message = "UserName must not be blank")
    @Size(min = 8,max = 12,message = "UserName must be between 8 and 12 characters")
    private String username;
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password must not be blank")
    @Size(min = 8,max = 12,message = "Password must be between 8 and 12 characters")
    private String password;
    private Set<String> roles;
    @NotBlank(message = "Phone Number must not be blank")
    @Pattern(regexp = "^\\d{10}$",message = "Invalid phone number format")
    private String  phone;
    @NotBlank(message = "Address must not be blank")
    private String  address;

    private Boolean locked;


}
