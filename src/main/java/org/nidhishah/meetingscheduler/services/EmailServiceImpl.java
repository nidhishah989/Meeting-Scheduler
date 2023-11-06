//package org.nidhishah.meetingscheduler.services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailServiceImpl implements EmailService{
//
//    @Autowired
//    private JavaMailSender emailSender;
//    @Override
//    public void sendEmail(String sendto, String sendfrom, String subject, String messageText) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(sendto);
//        message.setSubject(subject);
//        message.setText(messageText);
//        emailSender.send(message);
//    }
//}
