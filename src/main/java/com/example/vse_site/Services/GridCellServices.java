package com.example.vse_site.Services;

import com.example.vse_site.Entity.GridCell;
import com.example.vse_site.Entity.MyUser;
import com.example.vse_site.Entity.Transaction;
import com.example.vse_site.Repository.GridCellRepository;
import com.example.vse_site.Repository.MyUserRepository;
import com.example.vse_site.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GridCellServices {

    @Autowired
    private GridCellRepository gridCellRepository;

    @Autowired
    private MyUserRepository myUserRepository;


    // Načte všechny buňky mřížky
    public List<GridCell> getAllGridCells() {
        return gridCellRepository.findAll();
    }

    // Uloží nebo aktualizuje buňku mřížky
    public void saveGridCell(GridCell gridCell) {
        gridCellRepository.save(gridCell);
    }

    public Map<String, String> getGridColors() {
        Map<String, String> gridColors = new HashMap<>();

        // Příklad: Nahraďte tuto část databázovým dotazem nebo jiným způsobem
        // Jakmile získáte barvy, naplňte mapu následujícím způsobem:

        // Předpokládáme, že barvy získáme z databáze nebo jiného úložiště.
        // Tento příklad zobrazuje ručně zadané barvy pro mřížku 10x10.

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                // Klíč je formátovaný jako "row,col"
                // value je jen barva
                String key = row + "," + col;
                String color = getColorFromDatabase(row, col);  // metoda pro získání barvy z DB

                gridColors.put(key, color);
            }
        }
        return gridColors;
    }

        public Map<String, String> getGridColorsandUsers() {
            Map<String, String> gridColors = new HashMap<>();
            List<String> users = new ArrayList<>();
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    // Klíč je formátovaný jako "row,col"
                    // value je  barva, username
                    String key = row + "," + col;
                    String data = getColorandUserFromDatabase(row, col);  // metoda pro získání barvy z DB
                    String[] splitData = data.split(", ");
                    if(!users.contains(splitData[1]) && !splitData[1].equals("null")){
                        users.add(splitData[1]);
                    }
                    gridColors.put(key, data);
                }
            }

        return gridColors;
    }
    private String getColorFromDatabase(int row, int col) {
        return gridCellRepository.findByGridRowAndCol(row, col).getColor();
    }

    private String getColorandUserFromDatabase(int row, int col) {
        String name = "";
        String role = "";
        Long idUser = gridCellRepository.findByGridRowAndCol(row, col).getUser();
        if(idUser!=null){
            Optional<MyUser> user = myUserRepository.findById(idUser);
            name = user.isPresent() ? user.get().getUsername() : null;
            role = user.isPresent() ? user.get().getRole() : null;
        }else{ name = null;
        role = null;}
        return gridCellRepository.findByGridRowAndCol(row, col).getColor() +", " + name + ", "+ role;
    }

    public Map<String, Long> getFacultyCounts() {
        List<Object[]> results = gridCellRepository.countFacultyOccurrences().stream().toList();
        Map<String, Long> facultyCounts = new HashMap<>();
        System.out.println("services "+ results);
        for (Object[] result : results) {
            String faculty = (String) result[0];
            Long count = (Long) result[1];
            facultyCounts.put(faculty, count);
        }
        System.out.println("services "+ facultyCounts);

        return facultyCounts;
    }


}
