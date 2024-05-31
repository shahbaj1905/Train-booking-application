package com.shahbaj.IrctcApp.model;


import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PersonalDetail {
    private String name;
    private String aadhaarNo;
    private int age;
    private String mobileNo;
}
