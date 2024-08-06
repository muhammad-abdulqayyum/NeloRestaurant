package com.example.nelorestaurant.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Reservation {

    private int reservationId;

    private Table table;

    private Set<Diner> diners = new HashSet<>();

    private LocalDateTime reservationTime;

    private LocalDateTime endTime;


    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Set<Diner> getDiners() {
        return diners;
    }

    public void setDiners(Set<Diner> diners) {
        this.diners = diners;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
