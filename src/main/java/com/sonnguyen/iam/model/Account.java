package com.sonnguyen.iam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Data
@NoArgsConstructor
public class Account extends AbstractAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Email
    private String email;
    @NotNull
    @Size(min = 8,message = "Password must be at least 8 characters at length")
    private String password;
    private Boolean isEnabled;
    private Integer consecutiveLoginFailures;
    @Builder
    public Account(String email, String password, Boolean isEnabled, Integer consecutiveLoginFailures) {
        this.email = email;
        this.password = password;
        this.isEnabled = isEnabled;
        this.consecutiveLoginFailures = consecutiveLoginFailures;
    }
}
