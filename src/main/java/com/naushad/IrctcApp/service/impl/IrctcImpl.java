package com.naushad.IrctcApp.service.impl;

import com.naushad.IrctcApp.model.Passenger;
import com.naushad.IrctcApp.model.PersonalDetail;
import com.naushad.IrctcApp.model.Refund;
import com.naushad.IrctcApp.model.Ticket;
import com.naushad.IrctcApp.model.exception.ExpiredTicketException;
import com.naushad.IrctcApp.model.exception.InvalidPNRException;
import com.naushad.IrctcApp.model.exception.SeatNotFoundException;
import com.naushad.IrctcApp.repository.IrctcJdbcRepository;
import com.naushad.IrctcApp.repository.IrctcRepository;
import com.naushad.IrctcApp.service.IrctcInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class IrctcImpl implements IrctcInterface {

    @Autowired
    private IrctcRepository irctcRepository;

    @Autowired
    private IrctcJdbcRepository irctcJdbcRepository;

    @Override
    public Ticket bookTicket(Passenger passenger) {
        // 1. Availability
        // 2. yes - book. No - SeatNotAvailable
        boolean isSeatAvailable = irctcJdbcRepository.checkSeatIsAvailable(passenger.getTrainNo(),
                passenger.getNoOfSeats(),passenger.getDateOfJourney());
        if(!isSeatAvailable){
            // throw seatNotAvailable exception
            throw new SeatNotFoundException("Seat is not available");
        }
        return irctcJdbcRepository.bookTicket(passenger);
    }

    public Ticket checkPnrStatus(String pnr) {
        Ticket ticket = irctcRepository.checkPnrStatus(pnr);
        if(ticket == null){
            throw new InvalidPNRException("PNR is not valid - " + pnr);
        }
        return ticket;
    }
    public Refund cancelTicket(String pnr) {
        Ticket ticket=irctcRepository.checkPnrStatus(pnr);
        if(ticket == null){
            throw new InvalidPNRException("PNR is not valid");
        }
        Date dateOfJourney = ticket.getPassenger().getDateOfJourney();
        Date today = new Date();
        if(dateOfJourney.before(today)){
            throw new ExpiredTicketException("Ticket is expired");
        }
        return irctcRepository.cancelTicket(ticket);
    }
    public List<PersonalDetail> findAllPersonalDetails(){
       return irctcJdbcRepository.findAllPersonalDetails();
    }

    public PersonalDetail getPersonalDetailByAadhaarNo(String aadhaarNo){
        return irctcJdbcRepository.getPersonalDetailByAadhaarNo(aadhaarNo);
    }

    public List<PersonalDetail> findDetailByAge(int age){
        return irctcJdbcRepository.findDetailByAge(age);
    }

    public String deleteByAadhaarNo(String aadhaarNo){
        return irctcJdbcRepository.deleteByAadhaarNo(aadhaarNo);
    }
}
