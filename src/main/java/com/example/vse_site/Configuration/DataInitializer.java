package com.example.vse_site.Configuration;
import com.example.vse_site.Entity.GridCell;
import com.example.vse_site.Services.GridCellServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Autowired
    private GridCellServices gridCellService;

    @Bean
    public ApplicationRunner initializer() {
        return args -> {
            if (gridCellService.getAllGridCells().isEmpty()) {
                for (int row = 0; row < 10; row++) {
                    for (int col = 0; col < 10; col++) {
                        GridCell cell = new GridCell();
                        cell.setGridRow(row);
                        cell.setCol(col);
                        cell.setColor("#FFFFFF"); // Výchozí barva (bíla)
                        cell.setUser(null);
                        gridCellService.saveGridCell(cell);
                    }
                }
            }
        };
    }
}