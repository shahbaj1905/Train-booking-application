package com.shahbaj.IrctcApp.service;

import com.shahbaj.IrctcApp.model.Passenger;
import com.shahbaj.IrctcApp.model.PersonalDetail;
import com.shahbaj.IrctcApp.model.Refund;
import com.shahbaj.IrctcApp.model.Ticket;

import java.util.List;

public interface IrctcInterface {

    Ticket bookTicket(Passenger passenger);
    Refund cancelTicket(String pnr);
    Ticket checkPnrStatus(String pnr);

    List<PersonalDetail> findAllPersonalDetails();
    PersonalDetail getPersonalDetailByAadhaarNo(String aadhaarNo);
    List<PersonalDetail> findDetailByAge(int age);
    String deleteByAadhaarNo(String aadhaarNo);

}
