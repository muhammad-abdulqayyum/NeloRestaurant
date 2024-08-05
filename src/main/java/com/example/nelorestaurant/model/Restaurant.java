package com.example.nelorestaurant.model;

import java.util.HashSet;
import java.util.Set;

public class Restaurant {

    private int id;
    private String name;
    private Set<String> endorsements = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<String> getEndorsements() {
        return endorsements;
    }

    public void setEndorsements(Set<String> endorsements) {
        this.endorsements = endorsements;
    }

}
