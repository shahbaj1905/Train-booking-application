package com.shahbaj.IrctcApp.model;

import lombok.*;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    int id;
    private Passenger passenger;
    private double fare;
    private String PNR;
    private Date bookingDate;
}
