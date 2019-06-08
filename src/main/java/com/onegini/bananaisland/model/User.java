package com.onegini.bananaisland.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonIgnore
    private Long id;

    @JsonProperty("value")
    private int balance;

    @OneToMany
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
}
