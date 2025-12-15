package com.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");
        
        // SSL Configuration - Trust all certificates (DEVELOPMENT ONLY)
        // This bypasses SSL certificate validation
        try {
            // Set system properties to disable SSL verification globally (for this JVM)
            System.setProperty("mail.smtp.ssl.trust", "*");
            System.setProperty("mail.smtp.ssl.checkserveridentity", "false");
            
            // Create custom SSL socket factory
            TrustAllSSLSocketFactory sslSocketFactory = new TrustAllSSLSocketFactory();
            
            // Set SSL socket factory for STARTTLS
            props.put("mail.smtp.ssl.socketFactory", sslSocketFactory);
            props.put("mail.smtp.ssl.socketFactory.port", String.valueOf(port));
            props.put("mail.smtp.ssl.trust", "*");
            props.put("mail.smtp.ssl.checkserveridentity", "false");
            
            // Also set for direct SSL (port 465)
            props.put("mail.smtp.socketFactory", sslSocketFactory);
            props.put("mail.smtp.socketFactory.port", String.valueOf(port));
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to configure SSL for mail sender", e);
        }

        return mailSender;
    }
}

