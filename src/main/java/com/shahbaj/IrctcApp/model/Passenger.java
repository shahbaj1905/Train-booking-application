package com.shahbaj.IrctcApp.model;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Passenger {
    private int id;
    private String source;
    private String destination;
    private Date dateOfJourney;
    private int trainNo;
    private PersonalDetail personalDetail;
    private int noOfSeats;
    private Timestamp createdAt;

    private Constants.FoodType foodType;
}
