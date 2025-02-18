package models;

public class Commande {
    private int commande_id;
    private int id_user;
    private int id_panier;
    private String rue;
    private String ville;
    private String code_postal;
    private String etat;
    private float montant_total;
    private String methodePaiment; // Corrected the attribute name

    public Commande() {}

    public Commande(int commande_id, int id_panier, String rue, String ville, String code_postal, String etat, float montant_total, String methodePaiement, int id_user) {
        this.commande_id = commande_id;
        this.id_panier = id_panier;
        this.rue = rue;
        this.ville = ville;
        this.code_postal = code_postal;
        this.etat = etat;
        this.montant_total = montant_total;
        this.methodePaiment = methodePaiement;
        this.id_user = id_user;
    }

    public Commande(int commande_id, int id_user, int id_panier, String rue, String ville, String code_postal, String etat, float montant_total, String methodePaiment) {
        this.commande_id = commande_id;
        this.id_user = id_user;
        this.id_panier = id_panier;
        this.rue = rue;
        this.ville = ville;
        this.code_postal = code_postal;
        this.etat = etat;
        this.montant_total = montant_total;
        this.methodePaiment = methodePaiment;
    }

    // Getters and Setters
    public int getCommande_id() { return commande_id; }
    public void setCommande_id(int commande_id) { this.commande_id = commande_id; }

    public int getId_user() { return id_user; }
    public void setId_user(int id_user) { this.id_user = id_user; }

    public int getId_panier() { return id_panier; }
    public void setId_panier(int id_panier) { this.id_panier = id_panier; }

    public String getRue() { return rue; }
    public void setRue(String rue) { this.rue = rue; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getCode_postal() { return code_postal; }
    public void setCode_postal(String code_postal) { this.code_postal = code_postal; }

    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }

    public float getMontant_total() { return montant_total; }
    public void setMontant_total(float montant_total) { this.montant_total = montant_total; }

    public String getMethodePaiment() { return methodePaiment; }
    public void setMethodePaiment(String methodePaiement) { this.methodePaiment = methodePaiement; }

    @Override
    public String toString() {
        return "Commande{" +
                "commande_id=" + commande_id +
                ", id_user=" + id_user +
                ", id_panier=" + id_panier +
                ", rue='" + rue + '\'' +
                ", ville='" + ville + '\'' +
                ", code_postal='" + code_postal + '\'' +
                ", etat='" + etat + '\'' +
                ", montant_total=" + montant_total +
                ", methodePaiement='" + methodePaiment + '\'' +
                '}';
    }
}
