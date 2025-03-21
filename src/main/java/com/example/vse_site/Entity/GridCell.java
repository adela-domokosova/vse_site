package com.example.vse_site.Entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "grid_cells")
public class GridCell {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int gridRow; //řádek
    private int col; //sloupec
    private String color;  //barva formát HEX
    @Nullable
    private Long user; //userId
    @Nullable
    private String faculty;



}