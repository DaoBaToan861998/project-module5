package com.ra.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInForm {
    @NotBlank(message = "UserName must not be blank")
    @Size(min = 8,max = 12,message = "Name must be between 8 and 12 characters")
    private String username;
    @NotBlank(message = "Password must not be blank")
    @Size(min = 8,max = 12,message = "Password must be between 8 and 12 characters")
    private String password;

}
