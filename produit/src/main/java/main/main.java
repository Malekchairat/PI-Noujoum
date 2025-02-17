package main;

import controller.ServicesCrud;
import controller.PromotionCrud;
import entity.Promotion;
import entity.Produit;
import tools.DataSource;

import java.sql.SQLException;

import java.sql.Connection;

public class main {
    public static void main(String[] args) throws SQLException {

        Connection con = DataSource.getInstance().getConnection();
        ServicesCrud uc = new ServicesCrud();
        PromotionCrud pc = new PromotionCrud();
        Promotion promo = new Promotion(1, "PR24", 20.0f, "2010-12-31");
        Produit s1 = new Produit(4,"yahya","dernier", Produit.Categorie.albums,12,1,con.createBlob());
        //pc.add(promo);
        //uc.add(s1);
        //uc.delete(s1);
        pc.update(promo);

        //pc.delete();
    }

}
