package com.naushad.IrctcApp.repository;

import com.naushad.IrctcApp.model.*;
import com.naushad.IrctcApp.model.exception.BookingFailedException;
import com.naushad.IrctcApp.model.exception.PassengerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class IrctcJdbcRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${vegPrice}")
    private double vegPrice;
    @Value("${nonVegPrice}")
    private double nonVegPrice;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public List<PersonalDetail> findAllPersonalDetails() {
        return jdbcTemplate.query("select * from personalDetail", new BeanPropertyRowMapper<>(PersonalDetail.class));
    }

    public List<PersonalDetail> findDetailByAge(int age) {
        Object[] parameters = new Object[]{age};
        return jdbcTemplate.query("select * from personalDetail where age =?",parameters,
                new BeanPropertyRowMapper<>(PersonalDetail.class));
    }

    public PersonalDetail getPersonalDetailByAadhaarNo(String aadhaarNo){

        String query = "select * from personalDetail where aadhaarNo=" + "'" + aadhaarNo + "'";
//        Object[] parameters = new Object[]{aadhaarNo};
        PersonalDetail personalDetail = null;
        try{
            personalDetail = jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(PersonalDetail.class));
        }catch (DataAccessException dataAccessException){
            System.out.println("Didn't find any personal detail for aadhaarNo -" + aadhaarNo);
        }
        return personalDetail;
    }

    public String deleteByAadhaarNo(String aadhaarNo){
        Object[] parameters = new Object[]{aadhaarNo};

        int deletedRows = jdbcTemplate.update("delete from personalDetail where aadhaarNo=?",parameters);
       if(deletedRows > 0)
           return "Successfully deleted person with aadhaarNo - " + aadhaarNo;
       return "Not able to delete";
    }

    public Ticket bookTicket(Passenger passenger){
        /* Check person is already added in DB or not */
        boolean isAlreadyRegistered = alreadyRegistered(passenger.getPersonalDetail());
        boolean insertedPersonalDetail = true;

        /* If person is not added, add it in a DB */
        if(!isAlreadyRegistered){
            insertedPersonalDetail = insertPersonalDetail(passenger.getPersonalDetail());
        }
        Ticket ticket = null;
        /* If personal detail has been inserted, Insert passenger */
        if(insertedPersonalDetail){
            passenger.setCreatedAt(new Timestamp(new Date().getTime()));
            /* If passenger has been inserted, insert ticket */
            if(insertPassengerDetail(passenger)){
                passenger.setId(getPassengerId(passenger));
                ticket = generateTicket(passenger);
                /* If ticket is not generated, throw an exception */
                if(!insertTicket(ticket))
                    throw new BookingFailedException("Not able to book ticket");
            }
        }
        return ticket;
    }

    private Ticket generateTicket(Passenger passenger){
        Ticket ticket = new Ticket();
        ticket.setPassenger(passenger);
        ticket.setBookingDate(new Date());
        ticket.setFare(calculateFare(passenger));
        ticket.setPNR(generatePNR());
        return ticket;
    }

    private int getPassengerId(Passenger passenger) {
        String query = "select id from Passenger where date(dateOfJourney) = date(?)" +
                " and trainNo = ? and aadhaarNo = ? and createdAt between ? and ?";

        String formattedCreatedAt = simpleDateFormat.format(passenger.getCreatedAt());

        Object[] parameters = new Object[]{
                passenger.getDateOfJourney(),passenger.getTrainNo(),
                passenger.getPersonalDetail().getAadhaarNo(),
                formattedCreatedAt,getTimeAfter(passenger.getCreatedAt(),5)
        };
        Integer id = jdbcTemplate.queryForObject(query,Integer.class,parameters);
        if(id == null){
            throw new PassengerNotFoundException("Passenger is not available");
        }
        return id;
    }
    private String getTimeAfter(Timestamp timestamp,int sec){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());
        cal.add(Calendar.SECOND, sec);
        Timestamp newTimeStamp =  new Timestamp(cal.getTime().getTime());
        return simpleDateFormat.format(newTimeStamp);
    }

    private boolean insertTicket(Ticket ticket){
        String query = "insert into Ticket(fare,pnr,bookingDate,passengerId) " +
                "values(?,?,?,?)";
        Object[] parameters = new Object[]{
                ticket.getFare(),ticket.getPNR(),ticket.getBookingDate(),ticket.getPassenger().getId()
        };
        return jdbcTemplate.update(query,parameters) >= 0;
    }

    private boolean insertPassengerDetail(Passenger passenger){
        String query = "insert into Passenger(source,destination,dateOfJourney,trainNo,noOfSeats,foodType,aadhaarNo,createdAt) " +
                "values(?,?,?,?,?,?,?,?)";
        Object[] parameters = new Object[]{
                passenger.getSource(),passenger.getDestination(),passenger.getDateOfJourney(),passenger.getTrainNo(),
                passenger.getNoOfSeats(),passenger.getFoodType().name(),passenger.getPersonalDetail().getAadhaarNo(),
                passenger.getCreatedAt()
        };
        return jdbcTemplate.update(query,parameters) >= 0;
    }

    private boolean insertPersonalDetail(PersonalDetail personalDetail){
        String query = "Insert into personaldetail(name,aadhaarNo,age,mobileNo) values(?,?,?,?)";

       /* String query = "Insert into personaldetail(name,aadhaarNo,age,mobileNo) values(" +
                "'"+  personalDetail.getName() + "'," +"'"+  personalDetail.getAadhaarNo() + "',"
                + personalDetail.getAge() + ",'"+  personalDetail.getMobileNo() + "'" +
                ")";*/
        Object[] parameter = new Object[]{
                personalDetail.getName(),personalDetail.getAadhaarNo(),personalDetail.getAge(),personalDetail.getMobileNo()
        };
        int count = jdbcTemplate.update(query,parameter);
        return count > 0;
    }

    private boolean alreadyRegistered(PersonalDetail newPersonalDetail){
        PersonalDetail personalDetail = getPersonalDetailByAadhaarNo(newPersonalDetail.getAadhaarNo());
        if(personalDetail != null){
            if(personalDetail.equals(newPersonalDetail)){
                return true;
            }else{
                deleteByAadhaarNo(newPersonalDetail.getAadhaarNo());
                return false;
            }
        }
        return false;
    }

    public boolean checkSeatIsAvailable(int trainNo, int seatToBeBooked, Date dateOfJourney){
        int totalBookedSeat = countAllBookingByDateAndTrainNo(dateOfJourney,trainNo);
        Train train = getTrainByTrainNo(trainNo);
        return (train.getCapacity() - totalBookedSeat) >= seatToBeBooked;
    }

    public int countAllBookingByDateAndTrainNo(Date dateOfJourney,int trainNo){
        String query = "select sum(noOfSeats) from Passenger where dateOfJourney =? and trainNo =?";
        Object[] parameters = new Object[]{dateOfJourney,trainNo};
        Integer count = jdbcTemplate.queryForObject(query,parameters,Integer.class);
        if(count == null)
            return 0;
        return count;
    }

    public Train getTrainByTrainNo(int trainNo){
        String query = "select * from train where trainNo = ?";
        Object[] parameters = new Object[]{trainNo};
        return jdbcTemplate.queryForObject(query,parameters,new BeanPropertyRowMapper<>(Train.class));
    }

    public double calculateFare(Passenger passenger){
        Train train = getTrainByTrainNo(passenger.getTrainNo());
        double totalTicketPrice = train.getFare() * passenger.getNoOfSeats();
        if(Constants.FoodType.VEG.equals(passenger.getFoodType())){
            totalTicketPrice += (vegPrice* passenger.getNoOfSeats());
        } else if (Constants.FoodType.NON_VEG.equals(passenger.getFoodType())) {
            totalTicketPrice += (nonVegPrice* passenger.getNoOfSeats());
        }
        return totalTicketPrice;
    }

    public String generatePNR(){
        return (new Random().nextInt(1000)) + "";
    }

}
