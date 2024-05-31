package com.naushad.IrctcApp.service;

import com.naushad.IrctcApp.model.Passenger;
import com.naushad.IrctcApp.model.PersonalDetail;
import com.naushad.IrctcApp.model.Refund;
import com.naushad.IrctcApp.model.Ticket;

import java.util.Date;
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
