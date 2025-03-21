package com.example.vse_site.Repository;
import com.example.vse_site.Entity.GridCell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface GridCellRepository extends JpaRepository<GridCell, Long> {
    GridCell findByGridRowAndCol(int gridRow, int col);
    @Query("SELECT g.faculty, COUNT(g.faculty) FROM GridCell g WHERE g.faculty IS NOT NULL GROUP BY g.faculty")
    List<Object[]> countFacultyOccurrences();
    List<GridCell> findAllByUser(Long userID);
}