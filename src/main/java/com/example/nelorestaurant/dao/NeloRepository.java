package com.example.nelorestaurant.dao;

import com.example.nelorestaurant.dto.ReservationDTO;
import com.example.nelorestaurant.model.Diner;
import com.example.nelorestaurant.model.Reservation;
import com.example.nelorestaurant.model.Restaurant;
import com.example.nelorestaurant.model.Table;
import com.example.nelorestaurant.dto.TableDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class NeloRepository {

    //Logger
    private static final Logger log = LoggerFactory.getLogger(NeloRepository.class);

    //SQL Query Constants
    public static final String GET_ALL_RESERVATIONS_FOR_TIME
            = "SELECT * FROM reservations WHERE reservation_time >= ? and end_time <= ?";//todo: use this to ensure time
    public static final String GET_OPEN_TABLES = "SELECT * FROM tables WHERE capacity >= ?";
    public static final String GET_ALL_RESTAURANTS = "SELECT * FROM restaurants";
    public static final String DELETE_RESERVATION_VIA_ID = "DELETE FROM reservations WHERE id = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    //Row Mappers
    private static final RowMapper<ReservationDTO> RESERVATION_ROW_MAPPER = new RowMapper<>() {

        @Override
        public ReservationDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setReservationId(rs.getInt("id"));
            reservationDTO.setTableId(rs.getInt("table_id"));


            Array dinerIdsArray = rs.getArray("diner_ids");
            Integer[] dinerIds = (Integer[]) dinerIdsArray.getArray();
            reservationDTO.setDinerIds(new HashSet<>(Arrays.asList(dinerIds)));

            reservationDTO.setReservationTime(rs.getObject("reservation_time", LocalDateTime.class));
            reservationDTO.setEndTime(rs.getObject("end_time", LocalDateTime.class));


            return reservationDTO;
        }
    };

    private static final RowMapper<Restaurant> RESTAURANT_ROW_MAPPER = new RowMapper<>() {
        @Override
        public Restaurant mapRow(ResultSet rs, int rowNum) throws SQLException {
            Restaurant restaurant = new Restaurant();
            restaurant.setRestaurantId(rs.getInt("id"));
            restaurant.setRestaurantName(rs.getString("name"));

            String endorsementDetails = rs.getString("endorsements");
            String [] endorsements = endorsementDetails.split(",");

            List<String> endorsementList = new ArrayList<>(List.of(endorsements));
            endorsementList.replaceAll(String::toUpperCase);
            Set<String> endorsementSet = new HashSet<>(endorsementList);

            restaurant.setEndorsements(endorsementSet);

            log.debug("retrieved restaurant from DB: {}", restaurant);

            return restaurant;
        }
    };

    public static final RowMapper<TableDTO> TABLE_ROW_MAPPER = new RowMapper<>() {
        @Override
        public TableDTO mapRow(ResultSet rs, int rowNum) throws SQLException {

            TableDTO tableDTO = new TableDTO();

            tableDTO.setTableId(rs.getInt("id"));
            tableDTO.setTableCapacity(rs.getInt("capacity"));
            tableDTO.setRestaurantId(rs.getInt("restaurant_id"));

            return tableDTO;
        }
    };

    //DAO Methods

    public List<Reservation> getAllReservationsForTime(LocalDateTime startTime, LocalDateTime endTime) {

        List<ReservationDTO> reservationDtos = jdbcTemplate.query(GET_ALL_RESERVATIONS_FOR_TIME,
                new Object[]{startTime, endTime},
                new int[]{Types.TIMESTAMP, Types.TIMESTAMP},
                RESERVATION_ROW_MAPPER);

        List<Reservation> reservations = new ArrayList<>();
        Map<Integer, Set<Integer>> dinerMap = new HashMap();

        for (ReservationDTO reservationDTO : reservationDtos) {

            Reservation reservation = new Reservation();

            Table table = new Table();
            table.setTableId(reservationDTO.getTableId());
            reservation.setTable(table);

            dinerMap.put(reservationDTO.getReservationId(), reservationDTO.getDinerIds());

            reservation.setReservationId(reservationDTO.getReservationId());
            reservation.setReservationTime(reservationDTO.getReservationTime());
            reservation.setEndTime(reservationDTO.getEndTime());

            reservations.add(reservation);
        }

        //Initialize Diners for Reservation
        for (Reservation reservation: reservations) {
            Set<Integer> dinerIds = dinerMap.get(reservation.getReservationId());
            Set<Diner> diners = new HashSet<>();
            for (Integer dinerId: dinerIds) {
                Diner diner = new Diner();
                diner.setDinerId(dinerId);
                diners.add(diner);
            }
            reservation.setDiners(diners);
        }

        return reservations;
    }

    public List<Table> getOpenTablesFromDB(int groupSize, LocalDateTime time){
        List<TableDTO> openTableFromDB = jdbcTemplate.query(GET_OPEN_TABLES, new Object[]{groupSize}, TABLE_ROW_MAPPER);

        List<Table> openTables = new ArrayList<>();

        for(TableDTO tableDTO: openTableFromDB){
            Table table = new Table();
            table.setTableId(tableDTO.getTableId());
            table.setTableCapacity(tableDTO.getTableCapacity());

            Restaurant restaurant = new Restaurant();
            restaurant.setRestaurantId(tableDTO.getRestaurantId());

            table.setRestaurant(restaurant);

            openTables.add(table);
        }

        return openTables;
    }


    /**
     *
     * @return List of all Restaurants
     */
    public List<Restaurant> getAllRestaurants() {
        return jdbcTemplate.query(GET_ALL_RESTAURANTS,RESTAURANT_ROW_MAPPER);
    }

    /**
     *
     * Deletes reservation from DB based on reservationId param
     *
     * @param reservationId
     * @return
     */
    @Transactional
    public boolean deleteReservation(int reservationId){

        int rowsAffected = jdbcTemplate.update(DELETE_RESERVATION_VIA_ID, reservationId);

        if(rowsAffected > 0){
            return true; //Return true if at least one row was deleted
        } else {
            return false; //query delete error. todo: send specified message for processing error or 404 not found.
        }

    }

}
