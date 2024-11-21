package com.sonnguyen.iam.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class SendgridEmailServiceImpl extends AbstractEmailService {
    @Value("${service.mail.sendgrid.apiKey}")
    private String apiKey;

    @Override
    public void sendEmail(String dest, String subject, String body) {
        Email from = new Email(this.from);
        Email to = new Email(dest);
        Content content1 = new Content(MediaType.TEXT_PLAIN.getType(), body);
        Mail mail = new Mail(from, subject, to, content1);
        SendGrid sendGrid = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            if(response.getStatusCode() == 200) {
                log.info("Email sent successfully");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
