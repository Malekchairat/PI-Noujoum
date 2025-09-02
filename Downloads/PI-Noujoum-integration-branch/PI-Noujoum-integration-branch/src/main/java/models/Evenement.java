//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package models;

import java.util.Date;

public class Evenement {
    private int idEvenement;
    private String location;
    private String artist;
    private String description;
    private Date startDate;
    private Date endDate;
    private int time;
    private float price;
    private Type_e type;
    private int ticketCount;

    public Evenement() {
    }


    public Evenement(String location, String artist, String description, Date startDate, Date endDate, int time, float price, Type_e type, int ticketCount) {
        this.location = location;
        this.artist = artist;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
        this.price = price;
        this.type = type;
        this.ticketCount = ticketCount;
    }

    public Evenement(int idEvenement, String location, String artist, String description, Date startDate, Date endDate, int time, float price, Type_e type, int ticketCount) {
        this.idEvenement = idEvenement;
        this.location = location;
        this.artist = artist;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
        this.price = price;
        this.type = type;
        this.ticketCount = ticketCount;
    }
    public String getArtiste() {
        return this.artist;
    }


    public int getIdEvenement() {
        return this.idEvenement;
    }

    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Type_e getType() {
        return this.type;
    }

    public void setType(Type_e type) {
        this.type = type;
    }

    public int getTicketCount() {
        return this.ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public String toString() {
        int var10000 = this.idEvenement;
        return "Evenement{idEvenement=" + var10000 + ", location='" + this.location + "', artist='" + this.artist + "', description='" + this.description + "', startDate=" + String.valueOf(this.startDate) + ", endDate=" + String.valueOf(this.endDate) + ", time=" + this.time + ", price=" + this.price + ", type=" + String.valueOf(this.type) + ", ticketCount=" + this.ticketCount + "}";
    }
}
