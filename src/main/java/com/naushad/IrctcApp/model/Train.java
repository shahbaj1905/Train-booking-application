package com.naushad.IrctcApp.model;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Train {
    private int trainNo;
    private Constants.TrainType type;
    private int capacity;
    private double fare;
}
