package com.sonnguyen.iam.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractEmailService {
    @Value("${service.mail.from}")
    protected String from;
    abstract CompletableFuture<Void> sendEmail(String to, String subject, String body);
}
