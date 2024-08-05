package com.example.nelorestaurant.model;

import java.util.HashSet;
import java.util.Set;

public class Diner {

    private int id;
    private String name;
    private Set<String> dietaryRestrictions = new HashSet<>();

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(Set<String> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }
}
