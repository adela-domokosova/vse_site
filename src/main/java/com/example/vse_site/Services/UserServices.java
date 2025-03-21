package com.example.vse_site.Services;

import com.example.vse_site.CustomRandomStringGenerator;
import com.example.vse_site.Entity.MyUser;
import com.example.vse_site.Configuration.SecurityConfiguration.*;
import com.example.vse_site.Repository.MyUserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServices {

    @Autowired
    private MyUserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;


    public void register(MyUser user, String siteURL)
        throws UnsupportedEncodingException, MessagingException {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            String randomCode = CustomRandomStringGenerator.generate(64);
            user.setVerificationCode(randomCode);
            user.setEnabled(false);
            repo.save(user);

            sendVerificationEmail(user, siteURL);
        }
    public void changeToBePassword(MyUser user, String siteURL)
            throws UnsupportedEncodingException, MessagingException {
        System.out.println("user password from registration form:" + user.getPassword());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword_tobe(encodedPassword);
        Optional<MyUser> userDB = repo.findByEmail(user.getEmail());
        MyUser myUser = userDB.get();
        myUser.setPassword_tobe(encodedPassword);
        String randomCode = CustomRandomStringGenerator.generate(64);
        myUser.setVerificationCode(randomCode);
        repo.save(myUser);
        sendPasswordChangeEmail(myUser, siteURL);
    }

        private void sendVerificationEmail (MyUser user, String siteURL) throws MessagingException, UnsupportedEncodingException {
                String toAddress = user.getEmail();
                String fromAddress = "sitevse6@gmail.com";
                String senderName = "V$Epage";
                String subject = "Please verify your registration";
            String content = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verify Your Email</title>
   
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            color: #333333;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }
        .email-container {
            max-width: 600px;
            margin: 20px auto;
            background: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            font-family: 'Arial', sans-serif;
        }
        .email-header {
            background: linear-gradient(to right, #2a9d8f, #009688);
            color: #ffffff;
            padding: 20px;
            text-align: center;
        }
        .email-header h1 {
            margin: 0;
            font-size: 24px;
            font-weight: bold;
        }
        .email-body {
            padding: 20px;
        }
        .email-body p {
            margin: 10px 0;
            font-size: 16px;
        }
        .verify-button {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            color: #ffffff;
            background: #2a9d8f;
            text-decoration: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: bold;
     
           
        }
        .verify-button:hover {
            background: #00796b;
        }
        .email-footer {
            text-align: center;
            font-size: 12px;
            color: #888888;
            margin-top: 20px;
            padding: 10px;
            background-color: #f9f9f9;
            border-top: 1px solid #dddddd;
        }
  
    </style>
</head>
<body>
    <div class="email-container">
        <div class="email-header">
            <h1>Verify Your Email</h1>
        </div>
        <div class="email-body">
            <p>Dear [[name]],</p>
            <p>Thank you for registering at V$Epage. To complete your registration, please verify your email address by clicking the button below:</p>
            <a href="[[URL]]" class="verify-button" target="_self">Verify Your Email</a>
            <p>If you did not register for this account, you can ignore this email.</p>
            <p>Thank you,<br>The V$Epage Team</p>
        </div>
        <div class="email-footer">
            <p>&copy; 2025 V$Epage. All rights reserved.</p>
        </div>
    </div>
</body>
</html>
""";

            content = content.replace("[[name]]", user.getUsername());
            String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
            content = content.replace("[[URL]]", verifyURL);

            MimeMessage message = this.mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(content, true);

            this.mailSender.send(message);
        }



    private void sendPasswordChangeEmail (MyUser user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "sitevse6@gmail.com";
        String senderName = "V$Epage";
        String subject = "Please verify your password change";
        String content = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verify Your Email</title>
   
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            color: #333333;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }
        .email-container {
            max-width: 600px;
            margin: 20px auto;
            background: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            font-family: 'Arial', sans-serif;
        }
        .email-header {
            background: linear-gradient(to right, #2a9d8f, #009688);
            color: #ffffff;
            padding: 20px;
            text-align: center;
        }
        .email-header h1 {
            margin: 0;
            font-size: 24px;
            font-weight: bold;
        }
        .email-body {
            padding: 20px;
        }
        .email-body p {
            margin: 10px 0;
            font-size: 16px;
        }
        .verify-button {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            color: #ffffff;
            background: #2a9d8f;
            text-decoration: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: bold;
     
           
        }
        .verify-button:hover {
            background: #00796b;
        }
        .email-footer {
            text-align: center;
            font-size: 12px;
            color: #888888;
            margin-top: 20px;
            padding: 10px;
            background-color: #f9f9f9;
            border-top: 1px solid #dddddd;
        }
  
    </style>
</head>
<body>
    <div class="email-container">
        <div class="email-header">
            <h1>Verify Your Email</h1>
        </div>
        <div class="email-body">
            <p>Dear [[name]],</p>
            <p>Thank you for registering at V$Epage. To complete your registration, please verify your email address by clicking the button below:</p>
            <a href="[[URL]]" class="verify-button" target="_self">Verify Your Email</a>
            <p>If you did not register for this account, you can ignore this email.</p>
            <p>Thank you,<br>The V$Epage Team</p>
        </div>
        <div class="email-footer">
            <p>&copy; 2025 V$Epage. All rights reserved.</p>
        </div>
    </div>
</body>
</html>
""";

        content = content.replace("[[name]]", user.getUsername());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);

        this.mailSender.send(message);
    }

    public boolean verify(String verificationCode) {
        MyUser user = repo.findByVerificationCode(verificationCode);
        if (user == null || !user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            user.setRole("USER");
            repo.save(user);

            return true;
        }}

        public boolean verifyChangePassword(String verificationCode) {
            MyUser user = repo.findByVerificationCode(verificationCode);
            if (user == null || user.getPassword_tobe() == null) {
                return false;
            } else {
                user.setVerificationCode(null);
                System.out.println("passw" + user.getPassword());
                System.out.println("passw_tb" + user.getPassword_tobe());
                user.setPassword(user.getPassword_tobe());
                user.setPassword_tobe(null);
                repo.save(user);
                System.out.println("new passw" + user.getPassword());
                System.out.println(user);

                return true;
            }
    }


    public Boolean banUser(MyUser user, int num) {
        if(Objects.equals(num, 1)){
            user.setRole("BANNED");
        }else {
            user.setRole("USER");
        }
        repo.save(user);
        return true;
    }
    public boolean changePassword(MyUser user, String currentPassword, String newPassword) {
        if (passwordEncoder.matches(currentPassword, user.getPassword())) {
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedNewPassword);
            repo.save(user);
            return true;
        }
        return false;
    }

}