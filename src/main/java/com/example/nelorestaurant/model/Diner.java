package com.example.nelorestaurant.model;

import java.util.HashSet;
import java.util.Set;

public class Diner {

    private int dinerId;
    private String dinerName;
    private Set<String> dietaryRestrictions = new HashSet<>();

    public Diner(){}

    public Diner(int dinerId, String dinerName, Set<String> dietaryRestrictions) {
        this.dinerId = dinerId;
        this.dinerName = dinerName;
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public Diner(int dinerId, String dinerName) {
        this.dinerId = dinerId;
        this.dinerName = dinerName;
    }

    // Getters and Setters

    public int getDinerId() {
        return dinerId;
    }

    public void setDinerId(int dinerId) {
        this.dinerId = dinerId;
    }

    public String getDinerName() {
        return dinerName;
    }

    public void setDinerName(String dinerName) {
        this.dinerName = dinerName;
    }

    public Set<String> getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(Set<String> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }
}
