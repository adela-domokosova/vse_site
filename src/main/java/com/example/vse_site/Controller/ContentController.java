package com.example.vse_site.Controller;

import com.example.vse_site.Services.GridCellServices;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.example.vse_site.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
/*
Pro nepřihlášené uživatele, homepage login, registrace
*/

@Controller
public class ContentController {

    @Autowired
    private UserServices service;
    @Autowired
    private GridCellServices gridCellServices;

    @GetMapping("/home")
    public String handleHome(){
        return "home";
    }
    @GetMapping("/loginform")
    public String handleLogin(){
        return "login";
    }
    @GetMapping("/registration")
    public String handleRegistration(){
        return "register";
    }
    @GetMapping("/registration/success")
    public String handleRegistrationSuccess(){
        return "registration_verification/register_success";
    }
    @GetMapping("/registration/exists")
    public String handleRegistrationExists(){
        return "registration_verification/register_exists";
    }
    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (service.verify(code)) {
            return "registration_verification/verify_success";
        } else {
            return "registration_verification/verify_fail";
        }
    }
    @GetMapping("/verify/password/change")
    public String verifyPasswordChange(@Param("code") String code) {
        if (service.verifyChangePassword(code)) {
            System.out.println("yay");
            return "registration_verification/change_password_success";
        } else {
            System.out.println("nay");
            return "registration_verification/change_password_fail";
        }
    }

    @GetMapping("/unverified")
    public String handleUnverified(){
        return "registration_verification/unverified";
    }

    @GetMapping("/grid/colors")
    public ResponseEntity<Map<String, String>> getGridColors() {
        Map<String, String> gridColors = gridCellServices.getGridColors();
        return ResponseEntity.ok(gridColors);
    }

    @GetMapping("/grid/colorsusers")
    public ResponseEntity<Map<String, String>> getGridColorsUsers() {
        Map<String, String> gridColors = gridCellServices.getGridColorsandUsers();
        return ResponseEntity.ok(gridColors);
    }

    @GetMapping("/scoreboard")
    public String handleScoreboard(Model model) {
        Map<String, Long> count = gridCellServices.getFacultyCounts();
        model.addAttribute("count", count);
        return "scoreboard";
    }

    @GetMapping("/about")
    public String handleAbout(){
      return "about";
    }

    @GetMapping("/banned")
    public String handleBan(){
        return "banned";
    }
}
