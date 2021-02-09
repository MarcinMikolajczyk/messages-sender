package com.marcin.acaisoft.services;

import com.marcin.acaisoft.constant.Constant;
import com.marcin.acaisoft.models.Message;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void send(Message message){
        SimpleMailMessage _message = new SimpleMailMessage();
        _message.setFrom(Constant.SERVER_EMAIL);
        _message.setTo(message.getEmail());
        _message.setSubject(message.getTitle());
        _message.setText(message.getContent());
        javaMailSender.send(_message);
    }
}
