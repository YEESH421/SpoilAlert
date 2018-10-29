package com.dfreez3.spoilalert;

import java.util.Date;

public class FoodModel {

    private String name;
    private Date expirationDate;

    public FoodModel(String name, Date expirationDate) {
        this.name = name;
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return this.name;
    }

    public Date getExpirationDate() {
        return this.expirationDate;
    }

}
