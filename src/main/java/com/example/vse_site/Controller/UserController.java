package com.example.vse_site.Controller;

import com.example.vse_site.Entity.GridCell;
import com.example.vse_site.Entity.MyUser;
import com.example.vse_site.Entity.Transaction;
import com.example.vse_site.MyUserDetailService;
import com.example.vse_site.Repository.GridCellRepository;
import com.example.vse_site.Repository.MyUserRepository;
import com.example.vse_site.Repository.TransactionRepository;
import com.example.vse_site.Services.GridCellServices;
import com.example.vse_site.Services.TransactionServices;
import com.example.vse_site.Services.UserServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/*
Pro přihlášené uživatele, homepage, profil, buy pixels
*/
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServices service;
    @Autowired
    private MyUserDetailService myService;
    @Autowired
    private GridCellRepository gridCellRepository;
    @Autowired
    private GridCellServices gridCellServices;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private TransactionServices transactionServices;
    @Autowired
    private UserServices userServices;

    @GetMapping("/home")
    public String handleUserHome(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        MyUser user = myUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("NotFound"));
        Long userId = user.getId();

        // Fetch user by username
        Optional<MyUser> userOpt = myService.getUserByUsername(username);
        String transactionsJson = transactionServices.getTransactionsJsonByUserId(userId);

        if (userOpt.isPresent()) {

            return "user/home_user";
        } else {
            // User not found
            return "userNotFound";
        }

    }

    @GetMapping("/selectPixels")
    public String getGridPage(Model model) {
        List<GridCell> gridCells = gridCellServices.getAllGridCells();
        model.addAttribute("gridCells", gridCells);
        return "user/select_pixels_user"; // Název šablony HTML stránky, např. gridPage.html
    }

    @GetMapping("/order/recap")
    public String handleRecap(){
        return "user/order_recap_user";
    }

    @GetMapping("/payment/gateway")
    public String handlePaymentGateway(){
        return "user/payment_user";
    }

    @GetMapping("/payment/confirmation")
    public String handlePaymentConfirmation(){
        return "user/payment_confirmation_user";
    }

    @GetMapping("/info")
    public String handleUserInfo(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        MyUser user = myUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("NotFound"));
        Long userId = user.getId();

        // Fetch user by username
        Optional<MyUser> userOpt = myService.getUserByUsername(username);
        String transactionsJson = transactionServices.getTransactionsJsonByUserId(userId);

        if (userOpt.isPresent()) {
            MyUser myUser = userOpt.get();
            model.addAttribute("user", myUser);
            //případ kdy není transakce
            //jak předat ten list transakcí na front
            model.addAttribute("transactions", transactionsJson);
            return "user/info_user";
        } else {
            // User not found
            return "userNotFound";
        }}

    @PostMapping("/payment/process")
    public String processPayment(@RequestParam("cardNumber") String cardNumber,
                                 @RequestParam("expirationDate") String expirationDate,
                                 @RequestParam("cvv") String cvv,
                                 @RequestParam("amount") int amount,
                                 @RequestParam("changedCells") String changedCellsJson,
                                 Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        MyUser user = myUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("NotFound"));
        Long userId = user.getId();
        String userFaculty = user.getFaculty();

        // TRANSAKCE + SAVE
        Transaction transaction = new Transaction();
        transaction.setAmount(amount); // Cena je založena na počtu změněných buněk
        transaction.setUserId(userId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String formattedDate = sdf.format(new Date());
        transaction.setTimestamp(formattedDate);
        transaction.setCardNum(Long.parseLong(cardNumber));
        transaction.setPurchasedPixels(changedCellsJson);
        transactionRepository.save(transaction);

        //GRIDCELLS + SAVE
        //parse JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, String> map = objectMapper.readValue(changedCellsJson, Map.class);

            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                switch(value){
                    case "red":
                        value = "#ff0000";
                        break;
                    case "blue":
                        value = "#0000ff";
                        break;
                    case "green":
                        value = "#008000";
                        break;
                    case "yellow":
                        value = "#ffff00";
                        break;
                    case "orange":
                        value = "#ffa500";
                        break;
                    case "purple":
                        value = "#800080";
                        break;
                }

                String[] coordinates = key.split(",");
                int x = Integer.parseInt(coordinates[0]);//row
                int y = Integer.parseInt(coordinates[1]);//col

                GridCell cell = gridCellRepository.findByGridRowAndCol(x,y);
                cell.setUser(userId);
                cell.setColor(value);
                cell.setFaculty(userFaculty);
                gridCellRepository.save(cell);
            }

        } catch (IOException e) {
            e.printStackTrace();}


        return "redirect:/user/paymentConfirmation";
    }
    @GetMapping("/scoreboard")
    public String handleScoreboard(Model model){
        Map<String, Long> count = gridCellServices.getFacultyCounts();
        model.addAttribute("count", count);
        return "user/scoreboard";
    }

    @GetMapping("/about")
    public String handleAbout(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        MyUser user = myUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("NotFound"));
        Long userId = user.getId();

        // Fetch user by username
        Optional<MyUser> userOpt = myService.getUserByUsername(username);
        String transactionsJson = transactionServices.getTransactionsJsonByUserId(userId);

        if (userOpt.isPresent()) {

            return "user/about_user";
        } else {
            // User not found
            return "userNotFound";
        }
    }

    @PostMapping("/password/change")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        MyUser user = myUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isChanged = userServices.changePassword(user, currentPassword, newPassword);

        if (isChanged) {
            model.addAttribute("message", "Password updated successfully!");
        } else {
            model.addAttribute("error", "Incorrect current password!");
        }

        // Znova načítajte používateľa a transakcie pre profil
        String transactionsJson = transactionServices.getTransactionsJsonByUserId(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("transactions", transactionsJson);

        return "user/info_user"; // Vráťte používateľa späť na profil
    }
    @GetMapping("/change-password")
    public String showChangePasswordForm() {
        return "user/password_change"; // HTML formulár pre zmenu hesla
    }

}






