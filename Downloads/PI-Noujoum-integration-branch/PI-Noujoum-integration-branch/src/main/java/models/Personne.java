//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package models;

public class Personne {
    private int id;
    private int age;
    private String nom;
    private String prenom;

    public Personne() {
    }

    public Personne(int age, String nom, String prenom) {
        this.age = age;
        this.nom = nom;
        this.prenom = prenom;
    }

    public Personne(int id, int age, String nom, String prenom) {
        this.id = id;
        this.age = age;
        this.nom = nom;
        this.prenom = prenom;
    }

    public String toString() {
        return "Personne{id=" + this.id + ", age=" + this.age + ", nom='" + this.nom + "', prenom='" + this.prenom + "'}";
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
