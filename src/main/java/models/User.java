package models;

<<<<<<< HEAD
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
=======

import java.sql.SQLException;

import java.util.Objects;

import java.sql.Blob;

public class User {

    private int id_user, tel;
    private String nom, prenom, email, mdp, role;
    private Blob image; // Utilise java.sql.Blob

>>>>>>> origin/integration-branch
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

<<<<<<< HEAD
    // Getters and setters
=======
    public User(String nom, String prenom, String email, String mdp, int tel, String role, Blob image) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.tel = tel;
        this.role = role;
        this.image = image;
    }

    public User() {}

>>>>>>> origin/integration-branch
    public int getId() {
        return id_user;
    }

<<<<<<< HEAD
    public void setId(int id) {
        this.id_user = id;
=======
    public void setId(int id_user) {
        this.id_user = id_user;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
>>>>>>> origin/integration-branch
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

<<<<<<< HEAD
    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

=======
>>>>>>> origin/integration-branch
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
<<<<<<< HEAD
=======

    // Optionnel pour afficher l'image sous forme de String (si nécessaire)
    public String getImageAsString() throws SQLException {
        if (image != null) {
            byte[] imageBytes = image.getBytes(1, (int) image.length());
            return new String(imageBytes);
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.id_user != other.id_user) {
            return false;
        }
        if (this.tel != other.tel) {
            return false;
        }
        if (!Objects.equals(this.nom, other.nom)) {
            return false;
        }
        if (!Objects.equals(this.prenom, other.prenom)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.mdp, other.mdp)) {
            return false;
        }
        return Objects.equals(this.role, other.role);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id_user + ", nom=" + nom + ", prenom=" + prenom + ", email=" + email + ", mdp=" + mdp + ", tel=" + tel + ", role=" + role + ", image=" + image + '}';
    }
>>>>>>> origin/integration-branch
}
