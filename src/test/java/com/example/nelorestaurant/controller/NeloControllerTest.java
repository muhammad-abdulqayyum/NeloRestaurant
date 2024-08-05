package com.example.nelorestaurant.controller;

import com.example.nelorestaurant.model.Restaurant;
import com.example.nelorestaurant.model.Table;
import com.example.nelorestaurant.request.ReservationRequest;
import com.example.nelorestaurant.service.NeloService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class NeloControllerTest {

    @Mock
    private NeloService reservationService;

    @InjectMocks
    private NeloController reservationController;

    public NeloControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAvailableRestaurantTablesSuccess() {
        ReservationRequest request = new ReservationRequest(1);

        List<Table> tables = new ArrayList<>();
        Table table = new Table();
        table.setTableId(1);
        table.setTableCapacity(2);
        table.setRestaurant(new Restaurant());
        tables.add(table);

        when(reservationService.getMatchingTables(
                request.getGroupSize(),
                request.getTime(),
                request.getDietaryRestrictions())).thenReturn(tables);

        ResponseEntity<List<Table>> response = reservationController.findAvailableRestaurantTables(request);
        assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    public void testFindAvailableRestaurantTablesNotFound() {
        ReservationRequest request = new ReservationRequest(1);

        when(reservationService.getMatchingTables(
                request.getGroupSize(),
                request.getTime(),
                request.getDietaryRestrictions())).thenReturn(new ArrayList<Table>());

        ResponseEntity<List<Table>> response = reservationController.findAvailableRestaurantTables(request);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void testDeleteReservationSuccess() {
        int reservationId = 1;
        when(reservationService.deleteReservation(reservationId)).thenReturn(true);

        ResponseEntity<String> response = reservationController.deleteReservation(reservationId);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Reservation ID: 1 successfully deleted", response.getBody());
    }

    @Test
    public void testDeleteReservationNotFound() {
        int reservationId = 1;
        when(reservationService.deleteReservation(reservationId)).thenReturn(false);

        ResponseEntity<String> response = reservationController.deleteReservation(reservationId);
        assertEquals(404, response.getStatusCodeValue());
    }

}
