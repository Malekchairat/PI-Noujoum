package models;

import tools.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ticket {
    private int idTicket;
    private int idEvenement;
    private Evenement evenement;
    private int idUtilisateur;
    private float prix;
    private int quantite; // ✅ Changer float en int
    private String qrCode;
    private Type_P methodePaiement;

    public Ticket() {
    }

    // Constructeurs
    public Ticket(int idTicket, int idEvenement, int idUtilisateur, float prix, int quantite, String qrCode, Type_P methodePaiement) {
        this.idTicket = idTicket;
        this.idEvenement = idEvenement;
        this.idUtilisateur = idUtilisateur;
        this.prix = prix;
        this.quantite = quantite;
        this.qrCode = qrCode;
        this.methodePaiement = methodePaiement;
    }
<<<<<<< HEAD
    public String getEventName() {
        return (evenement != null) ? evenement.getArtist() : "Événement inconnu";
    }

<<<<<<< HEAD

=======
>>>>>>> origin/integration-branch
=======

>>>>>>> origin/GestionCommande
    public Ticket(Evenement evenement, int idUtilisateur, float prix, int quantite, String qrCode, Type_P methodePaiement) {
        this.evenement = evenement;
        this.idUtilisateur = idUtilisateur;
        this.prix = prix;
        this.quantite = quantite;
        this.qrCode = qrCode;
        this.methodePaiement = methodePaiement;
    }

    // Getters et Setters
    public int getIdTicket() {
        return this.idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public int getIdEvenement() {
        return this.idEvenement;
    }

    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }

    public int getIdUtilisateur() {
        return this.idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public float getPrix() {
        return this.prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getQuantite() { // ✅ Correction ici (retourne un int)
        return this.quantite;
    }

    public void setQuantite(int quantite) { // ✅ Correction ici (prend un int)
        this.quantite = quantite;
    }

    public String getQrCode() {
        return this.qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Type_P getMethodePaiement() {
        return this.methodePaiement;
    }

    public void setMethodePaiement(Type_P methodePaiement) {
        this.methodePaiement = methodePaiement;
    }
<<<<<<< HEAD
    public Evenement getEvenement() {
        return this.evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

=======
>>>>>>> origin/GestionCommande
}
