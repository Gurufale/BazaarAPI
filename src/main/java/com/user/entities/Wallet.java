package com.user.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wallets")
@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int balance;
    private String note;

    public void setBalance(int newBalance) {
        this.balance = newBalance;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
