package entity;

import tools.DataSource;

import java.sql.Connection;

public class Promotion {
    private int idpromotion;
    private String code;
    private float pourcentage;
    private String expiration;
    private Connection cnx;

    public Promotion(int idpromotion, String code, float pourcentage, String expiration) {
        this.idpromotion = idpromotion;
        this.code = code;
        this.pourcentage = pourcentage;
        this.expiration = expiration;
        this.cnx = DataSource.getInstance().getConnection();
    }


    public Promotion(String promo, float percent, String date) {
    }

    public int getIdpromotion() { return idpromotion; }
    public String getCode() { return code; }
    public float getPourcentage() { return pourcentage; }
    public String getExpiration() { return expiration; }
    public Connection getCnx() { return cnx; }

    public void setCode(String code) { this.code = code; }
    public void setPourcentage(float pourcentage) { this.pourcentage = pourcentage; }
    public void setExpiration(String expiration) { this.expiration = expiration; }

    @Override
    public String toString() {
        return "Promotion{" +
                "idpromotion=" + idpromotion +
                ", code='" + code + '\'' +
                ", pourcentage=" + pourcentage +
                ", expiration='" + expiration + '\'' +
                ", cnx=" + cnx +
                '}';
    }
}
