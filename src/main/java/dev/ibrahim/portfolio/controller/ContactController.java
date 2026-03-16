package dev.ibrahim.portfolio.controller;

import dev.ibrahim.portfolio.dto.request.ContactRequest;
import dev.ibrahim.portfolio.dto.response.ApiResponse;
import dev.ibrahim.portfolio.service.ContactService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    /**
     * POST /api/contact
     * Saves message to DB and sends notification email
     *
     * Request body:
     * {
     *   "name": "John Doe",
     *   "email": "john@example.com",
     *   "message": "Hello Ibrahim..."
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> sendMessage(
            @Valid @RequestBody ContactRequest request,
            HttpServletRequest httpRequest) {

        contactService.processContactForm(request, httpRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Message received! I'll get back to you soon.", null));
    }
}
