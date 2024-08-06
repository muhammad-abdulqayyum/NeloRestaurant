package com.example.nelorestaurant.request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class SearchReservationRequest {

    private int groupSize;
    private List<String> dietaryRestrictions;
    private LocalDateTime time;

    /**
     * Contstructor to intialize all values
     *
     * @param groupSize
     * @param dietaryRestrictions
     * @param time
     */
    public SearchReservationRequest(int groupSize, List<String> dietaryRestrictions, LocalDateTime time) {
        this.groupSize = groupSize;
        this.dietaryRestrictions = dietaryRestrictions;
        this.time = time;
    }

    /**
     * Constructor initializes groupSize and dietaryRestrictions. Will set time to current time by default.
     *
     * @param groupSize
     * @param dietaryRestrictions
     */
    public SearchReservationRequest(int groupSize, List<String> dietaryRestrictions) {
        this.groupSize = groupSize;
        this.dietaryRestrictions = dietaryRestrictions;
        this.time = LocalDateTime.now();
    }

    /**
     * Constructor sets groupSize and time. Sets dietaryRestrictions to an empty list by default
     * @param groupSize
     * @param time
     */
    public SearchReservationRequest(int groupSize, LocalDateTime time) {
        this.groupSize = groupSize;
        this.dietaryRestrictions = new ArrayList<>();
        this.time = time;
    }

    /**
     * Constructor initializes groupSize. Sets dietaryRestrictions to empty list by default
     * and time to present time by default.
     *
     * @param groupSize
     */
    public SearchReservationRequest(int groupSize) {
        this.groupSize = groupSize;
        this.dietaryRestrictions = new ArrayList<>();
        this.time = LocalDateTime.now();
    }

    /**
     * Default constuctor takes in no values. Sets groupSize to "1" by default,
     * with no dietaryRestrictions and present time for request
     */
    public SearchReservationRequest() {
        this.groupSize = 1;
        this.dietaryRestrictions = new ArrayList<>();
        this.time = LocalDateTime.now();
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public List<String> getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(List<String> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
