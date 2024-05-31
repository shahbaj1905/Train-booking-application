package com.shahbaj.IrctcApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Refund {
    private Ticket ticket;
    private double refundedAmount;
    private double deductedAmount;
    private Date refundDate;
}
