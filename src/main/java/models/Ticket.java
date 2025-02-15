//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package models;

public class Ticket {
    private int idTicket;
    private int idEvenement;
    private Evenement evenement;
    private int idUtilisateur;
    private float prix;
    private String qrCode;
    private Type_P methodePaiement;

    public Ticket() {
    }

    public Ticket(int idTicket, int idEvenement, int idUtilisateur, float prix, String qrCode, Type_P methodePaiement) {
        this.idTicket = idTicket;
        this.idEvenement = idEvenement;
        this.idUtilisateur = idUtilisateur;
        this.prix = prix;
        this.qrCode = qrCode;
        this.methodePaiement = methodePaiement;
    }

    public Ticket(Evenement evenement, int idUtilisateur, float prix, String qrCode, Type_P methodePaiement) {
        this.evenement = evenement;
        this.idUtilisateur = idUtilisateur;
        this.prix = prix;
        this.qrCode = qrCode;
        this.methodePaiement = methodePaiement;
    }

    public Evenement getEvenement() {
        return this.evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

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
}
