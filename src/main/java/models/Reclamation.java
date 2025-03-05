package models;


import java.util.Date;
import java.util.Objects;

public class Reclamation {
    private int id;

    private User user;
    private String titre;
    private String description;
    private Date dateCreation;
    private String statut;  // "En attente", "Traitée"
    private String priorite;  // "Basse", "Moyenne", "Haute"
    private String answer;

    public Reclamation(int id, User user, String titre, String description, Date dateCreation, String statut, String priorite, String answer) {
        this.id = id;
        this.user = user;
        this.titre = titre;
        this.description = description;
        this.dateCreation = dateCreation;
        this.statut = statut;
        this.priorite = priorite;
        this.answer = answer;
    }


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Reclamation(int id, User user, String titre, String description, Date dateCreation, String statut, String priorite) {
        this.id = id;
        this.user = user;
        this.titre = titre;
        this.description = description;
        this.dateCreation = dateCreation;
        this.statut = statut;
        this.priorite = priorite;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public Reclamation() {}

    public Reclamation(int id, String titre, String description, Date dateCreation, String statut, String priorite) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateCreation = dateCreation;
        this.statut = statut;
        this.priorite = priorite;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getDateCreation() { return dateCreation; }
    public void setDateCreation(Date dateCreation) { this.dateCreation = dateCreation; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getPriorite() { return priorite; }
    public void setPriorite(String priorite) { this.priorite = priorite; }



    @Override
    public String toString() {
        return "Réclamation{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", dateCreation=" + dateCreation +
                ", statut='" + statut + '\'' +
                ", priorite='" + priorite + '\'' +
                "answer : " +answer+
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reclamation that = (Reclamation) o;
        return id == that.id && Objects.equals(titre, that.titre) && Objects.equals(description, that.description) && Objects.equals(dateCreation, that.dateCreation) && Objects.equals(statut, that.statut) && Objects.equals(priorite, that.priorite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titre, description, dateCreation, statut, priorite);
    }
}

