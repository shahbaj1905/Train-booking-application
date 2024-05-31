package com.shahbaj.IrctcApp.controller;


import com.shahbaj.IrctcApp.model.Passenger;
import com.shahbaj.IrctcApp.model.PersonalDetail;
import com.shahbaj.IrctcApp.model.Refund;
import com.shahbaj.IrctcApp.model.Ticket;
import com.shahbaj.IrctcApp.service.IrctcInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
public class IrctcController {

    @Autowired
    private IrctcInterface irctcInterface;

    @PostMapping("/book")
    public ResponseEntity<Object> bookTicket(@RequestBody Passenger passenger, WebRequest webRequest){
        Ticket ticket = irctcInterface.bookTicket(passenger);

        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @GetMapping("/checkPnrStatus")
    public Ticket checkPnrStatus(@RequestParam String pnr){
        return irctcInterface.checkPnrStatus(pnr);
    }
    @DeleteMapping("/cancelTicket")
    public Refund cancelTicket(@RequestBody String pnr) {
        return irctcInterface.cancelTicket(pnr);
    }


    @GetMapping("/getAll")
    List<PersonalDetail> findAllPersonalDetails(){
        return irctcInterface.findAllPersonalDetails();
    }

    @GetMapping("/getPD/{aadhaarNo}")
    public PersonalDetail getPersonalDetailByAadhaarNo(@PathVariable String aadhaarNo){
        return irctcInterface.getPersonalDetailByAadhaarNo(aadhaarNo);
    }

    @GetMapping("/getByAge/{age}")
    List<PersonalDetail> findDetailByAge(@PathVariable int age){
        return irctcInterface.findDetailByAge(age);
    }

    @DeleteMapping("/delete/{aadhaarNo}")
    public String deleteByAadhaarNo(@PathVariable String aadhaarNo){
        return irctcInterface.deleteByAadhaarNo(aadhaarNo);
    }

}
