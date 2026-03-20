package model;

public class Appointment {
    private int id;
    private String doctor;
    private String date;
    private String time;
    private String reason;
    private String status;

    public Appointment(int id, String doctor, String date, String time, String reason, String status) {
        this.id = id;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.status = status;
    }

    public int getId()         { return id; }
    public String getDoctor()  { return doctor; }
    public String getDate()    { return date; }
    public String getTime()    { return time; }
    public String getReason()  { return reason; }
    public String getStatus()  { return status; }
}