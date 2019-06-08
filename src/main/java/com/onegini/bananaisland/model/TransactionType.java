package com.onegini.bananaisland.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TransactionType {
    @JsonProperty("decrease")
    DECREASE,

    @JsonProperty("increase")
    INCREASE;
}
