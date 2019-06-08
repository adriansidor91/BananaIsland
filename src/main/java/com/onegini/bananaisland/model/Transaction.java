package com.onegini.bananaisland.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    @JsonIgnore
    private Long transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    @JsonProperty("type")
    private TransactionType transactionType;

    private int value;

}
