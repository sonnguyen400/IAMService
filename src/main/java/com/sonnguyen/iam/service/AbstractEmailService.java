package com.sonnguyen.iam.service;

import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractEmailService {
    @Value("${service.mail.from}")
    protected String from;
    abstract void sendEmail(String to, String subject, String body);
}
