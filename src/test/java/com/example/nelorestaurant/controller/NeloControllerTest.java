package com.example.nelorestaurant.controller;

import com.example.nelorestaurant.service.NeloService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

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
    public void deleteReservation_success() {
        int reservationId = 1;
        when(reservationService.deleteReservation(reservationId)).thenReturn(true);

        ResponseEntity<String> response = reservationController.deleteReservation(reservationId);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Reservation ID: 1 successfully deleted", response.getBody());
    }

    @Test
    public void deleteReservation_notFound() {
        int reservationId = 1;
        when(reservationService.deleteReservation(reservationId)).thenReturn(false);

        ResponseEntity<String> response = reservationController.deleteReservation(reservationId);
        assertEquals(404, response.getStatusCodeValue());
    }

}
