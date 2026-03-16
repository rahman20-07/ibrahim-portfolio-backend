package dev.ibrahim.portfolio.service;

import dev.ibrahim.portfolio.dto.request.ContactRequest;
import dev.ibrahim.portfolio.model.ContactMessage;
import dev.ibrahim.portfolio.repository.ContactMessageRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    private final ContactMessageRepository contactMessageRepository;
    private final JavaMailSender mailSender;

    @Value("${app.contact.recipient-email}")
    private String recipientEmail;

    @Transactional
    public void processContactForm(ContactRequest request, HttpServletRequest httpRequest) {
        log.info("Processing contact form from: {}", request.getEmail());

        // 1. Persist to DB
        ContactMessage message = ContactMessage.builder()
                .name(request.getName())
                .email(request.getEmail())
                .message(request.getMessage())
                .ipAddress(getClientIp(httpRequest))
                .read(false)
                .build();

        contactMessageRepository.save(message);
        log.info("Contact message saved with id: {}", message.getId());

        // 2. Send notification email
        try {
            sendNotificationEmail(request);
        } catch (Exception e) {
            // Email failure should NOT fail the whole request
            // Message is already saved in DB — that's the source of truth
            log.error("Failed to send notification email for contact from {}: {}",
                    request.getEmail(), e.getMessage());
        }
    }

    private void sendNotificationEmail(ContactRequest request) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(recipientEmail);
        mail.setSubject("[Portfolio] New message from " + request.getName());
        mail.setText(String.format(
                "Name: %s%nEmail: %s%n%nMessage:%n%s",
                request.getName(),
                request.getEmail(),
                request.getMessage()
        ));
        mailSender.send(mail);
        log.info("Notification email sent for contact from: {}", request.getEmail());
    }

    // Extract real IP — handles proxies and load balancers
    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
