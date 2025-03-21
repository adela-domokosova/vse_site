package com.example.vse_site.Controller;

import com.example.vse_site.Entity.GridCell;
import com.example.vse_site.Entity.MyUser;
import com.example.vse_site.Repository.GridCellRepository;
import com.example.vse_site.Repository.MyUserRepository;
import com.example.vse_site.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/*
Pro admina, jen homepage a mazání píxelů
*/
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserServices service;
    @Autowired
    private GridCellRepository gridCellRepository;
    @Autowired
    private MyUserRepository myUserRepository;


    @GetMapping("/home")
    public String handleAdminHome(){
        return "admin/home_admin";
    }

    @GetMapping("/pixels/delete")
    public String handleDeletePixels(){
        return "admin/delete_pixels_admin";
    }

    @PostMapping("/ban")
    public ResponseEntity<String> banUser(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        int number = Integer.parseInt(payload.get("number"));
        Optional<MyUser> optUser = myUserRepository.findByUsername(username);
        if (optUser.isPresent()) {
            MyUser user = optUser.get();
            boolean success = service.banUser(user, number);
            if (success) {
                return ResponseEntity.ok("User role updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update user role");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed find user");
        }
    }
    @PostMapping("/pixels/save")
    public ResponseEntity<Map<String, String>> handleSaveDelete(@RequestBody Map<String, String> changedCells){
        for (Map.Entry<String, String> entry : changedCells.entrySet()) {
            String key = entry.getKey();
            String[] coordinates = key.split(",");
            int x = Integer.parseInt(coordinates[0]);//row
            int y = Integer.parseInt(coordinates[1]);//col
        GridCell cell = gridCellRepository.findByGridRowAndCol(x, y);
        cell.setUser(null);
        cell.setColor("#FFFFFF");
        cell.setFaculty(null);
        gridCellRepository.save(cell);

}
        Map<String, String> response = new HashMap<>();
        response.put("message", "Changes saved successfully.");
        response.put("redirect", "/admin/home");
        System.out.println("response "+ response);
        return ResponseEntity.ok(response);
    }
}