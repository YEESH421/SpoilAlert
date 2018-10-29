package com.dfreez3.spoilalert;

import java.util.Date;

public class FoodModel {

    private String name;
    private Date purchaseDate;
    private long expirationPeriod;

    public FoodModel(String name, Date purchaseDate, long expirationPeriod) {
        this.name = name;
        this.purchaseDate = purchaseDate;
        this.expirationPeriod = expirationPeriod;
    }

    public String getName() {
        return this.name;
    }

    public Date getPurchaseDate() {
        return this.purchaseDate;
    }

    public long getExpirationPeriod() {
        return this.expirationPeriod;
    }

}