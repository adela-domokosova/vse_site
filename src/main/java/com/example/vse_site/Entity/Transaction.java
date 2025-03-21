package com.example.vse_site.Entity;

import com.example.vse_site.Entity.GridCell;
import com.example.vse_site.Entity.MyUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int amount;  // částka
    private Long userId;    // ID uživatele (odkaz na tabulku MyUser)
    private String timestamp; // Čas, kdy transakce probíhla
    private Long cardNum;
    private String purchasedPixels;  // Seznam nakoupených pixelů

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", userId=" + userId +
                ", timestamp=" + timestamp +
                ", cardNum=" + cardNum +
                ", purchasedPixels='" + purchasedPixels + '\'' +
                '}';
    }
}

