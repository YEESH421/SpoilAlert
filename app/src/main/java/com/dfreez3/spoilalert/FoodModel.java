package com.dfreez3.spoilalert;

import java.util.Date;

public class FoodModel {

    private int id;
    private String name;
    private Date purchaseDate;
    private long expirationPeriod;

    public FoodModel(int id, String name, Date purchaseDate, long expirationPeriod) {
        this.id = id;
        this.name = name;
        this.purchaseDate = purchaseDate;
        this.expirationPeriod = expirationPeriod;
    }

    public int getId() {
        return this.id;
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