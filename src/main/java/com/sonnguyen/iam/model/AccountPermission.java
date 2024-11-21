package com.sonnguyen.iam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public Long account_id;
    public Long permission_id;
}
