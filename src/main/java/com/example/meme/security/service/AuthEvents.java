package com.example.meme.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthEvents {

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        log.info("Successful login for user: {}" , success.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failure) {
        log.info("Failed login for user: {} due to: {}",
                failure.getAuthentication().getName(),
                failure.getException().getMessage());
    }
}
