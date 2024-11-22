package com.sonnguyen.iam.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ForbiddenToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "text")
    private String token;

    public ForbiddenToken() {
    }

    public ForbiddenToken(String token) {
        this.token = token;
    }
}
