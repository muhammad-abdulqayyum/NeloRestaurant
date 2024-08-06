package com.example.nelorestaurant.response;

import com.example.nelorestaurant.model.Diner;
import com.example.nelorestaurant.model.Table;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ReservationResponse {

    private int reservationId;

    private Table table;

    private Set<Diner> diners = new HashSet<>();

    private LocalDateTime time;

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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
