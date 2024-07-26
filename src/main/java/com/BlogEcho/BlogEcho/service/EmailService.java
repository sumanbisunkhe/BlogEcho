package com.BlogEcho.BlogEcho.service;

import com.BlogEcho.BlogEcho.dto.EmailDto;

public interface EmailService {
    void sendEmail(EmailDto emailDto);
}
