package com.example.vse_site.Controller;

import com.example.vse_site.Entity.MyUser;
import com.example.vse_site.Configuration.SecurityConfiguration.*;
import com.example.vse_site.Repository.MyUserRepository;
import com.example.vse_site.Services.UserServices;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@RestController
public class RegistrationController {
    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private UserServices service;


    @PostMapping("/register")
        public void processRegister(@ModelAttribute MyUser user, HttpServletRequest request, HttpServletResponse response)
            throws MessagingException, IOException {
        if (myUserRepository.findByEmail(user.getEmail()).isPresent()){
            //změnit password to be
            service.changeToBePassword(user, getSiteURL(request));
            //ange role to USER
            //poslat verigfication email -> verifikací se z tobe hesla stane heslo
            response.sendRedirect("/registration/exists");
        }else{
        if (user.getEmail() != null && user.getEmail().length() >= 6) {
                user.setUsername(user.getEmail().substring(0, 6));
            }
            user.setRole("UNVERIFIED");
            service.register(user, getSiteURL(request));
            response.sendRedirect("/registration/success");
        }}

        private String getSiteURL(HttpServletRequest request) {
            String siteURL = request.getRequestURL().toString();
            return siteURL.replace(request.getServletPath(), "");
        }

    }


