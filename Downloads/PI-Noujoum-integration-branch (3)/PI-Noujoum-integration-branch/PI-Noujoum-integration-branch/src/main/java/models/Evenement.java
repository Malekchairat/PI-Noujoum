package models;

import java.sql.Blob;
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
    private Blob imageE;  // Nouveau champ pour l'image

    public Evenement() {
    }
    public Evenement(int idEvenement, String location, String artist, String description, Date startDate, Date endDate, int time, float price, Type_e type, int ticketCount, Blob imageE) {
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
        this.imageE = imageE;
    }

    public Evenement(String location, String artist, String description, Date startDate, Date endDate, int time, float price, Type_e type, int ticketCount, Blob imageE) {
        this.location = location;
        this.artist = artist;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
        this.price = price;
        this.type = type;
        this.ticketCount = ticketCount;
        this.imageE = imageE;
    }

    // Getters and Setters
    public int getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Type_e getType() {
        return type;
    }

    public void setType(Type_e type) {
        this.type = type;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public Blob getImageE() {
        return imageE;
    }

    public void setImageE(Blob imageE) {
        this.imageE = imageE;
    }

    @Override
    public String toString() {
        return "Evenement{idEvenement=" + idEvenement + ", location='" + location + "', artist='" + artist + "', description='" + description + "', startDate=" + startDate + ", endDate=" + endDate + ", time=" + time + ", price=" + price + ", type=" + type + ", ticketCount=" + ticketCount + "}";
    }
    public Blob getImage() {
        return imageE;
    }

    public void setImage(Blob image) {
        this.imageE = image;
    }
}
