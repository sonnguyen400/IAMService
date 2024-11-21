package com.sonnguyen.iam.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_profile")
public class UserProfile extends AbstractAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull(message = "User's profile must be connected to an account")
    private Long account_id;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private String gender;
    private String picture_url;
}
