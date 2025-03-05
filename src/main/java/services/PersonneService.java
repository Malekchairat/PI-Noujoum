//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Personne;
import tools.MyDataBase;

public class PersonneService implements IService<Personne> {
    private Connection cnx = MyDataBase.getInstance().getCnx();

    public PersonneService() {
    }

    public void ajouter(Personne p) throws SQLException {
        String sql = "insert into personne(nom,prenom,age) values(?,?,?)";
        PreparedStatement st = this.cnx.prepareStatement(sql);
        st.setString(1, p.getNom());
        st.setString(2, p.getPrenom());
        st.setInt(3, p.getAge());
        st.executeUpdate();
        System.out.println("Personne ajoutée");
    }

    public void supprimer(Personne p) {
    }

    public void modifier(Personne p, String nom) {
    }

    public List<Personne> recuperer() throws SQLException {
        String sql = "select * from personne";
        Statement st = this.cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Personne> personnes = new ArrayList();

        while(rs.next()) {
            Personne p = new Personne();
            p.setAge(rs.getInt("age"));
            p.setPrenom(rs.getString("prenom"));
            p.setNom(rs.getString("nom"));
            p.setId(rs.getInt("id"));
            personnes.add(p);
        }

        return personnes;
    }
}
