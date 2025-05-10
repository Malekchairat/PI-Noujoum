package models;

import java.sql.Blob;

public class User {
    private int id_user;
    private String nom;
    private String prenom;
    private String email;
    private String mdp;
    private int tel;
    private String role;
    private Blob image; // The image is of Blob type

    // Default constructor
    public User() {}

    // Constructor with all fields
    public User(int id_user, String nom, String prenom, String email, String mdp, int tel, String role, Blob image) {
        this.id_user = id_user;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.tel = tel;
        this.role = role;
        this.image = image;
    }
    public User(int id, String name, String email, int phone, Blob profilePicture) {
        this.id_user = id;
        this.nom = name;
        this.email = email;
        this.tel = phone;
        this.image = profilePicture;
    }
    // Getters and setters
    public int getId() {
        return id_user;
    }

    public void setId(int id) {
        this.id_user = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
}
