package Petljanje_sa_bazom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Bosko
 */
public abstract class Baza_rudara
{
    public static Connection veza;
    protected static String naredba;
    
    //Metoda uspostavlja vezu sa bazom podataka
    public static void initDB()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            veza = DriverManager.getConnection("jdbc:mysql://localhost/bitcoin", "root", "");
        }
        catch(ClassNotFoundException | SQLException n){}
    }
    
    //Metoda zatvara vezu sa bazom podataka
    public static void zatvoriBazu()
    {
        try { veza.close(); }
        catch(SQLException n) { n.printStackTrace(); }
    }
    
    /*Metoda uzima rudare iz baze, i stavlja ih u ObservableListu koju vraca
    kao rezultat*/
    public static ObservableList<Rudar> vrati_rudare()
    {
        ObservableList<Rudar> miners = FXCollections.observableArrayList();
        String sql = "SELECT * FROM `rudari`";
        
        Baza_rudara.initDB();
        
        try
        {
            Statement iskaz = veza.createStatement();
            ResultSet rs = iskaz.executeQuery(sql);
            
            while(rs.next())
            {
                String ime = rs.getString(1);
                String sifra = rs.getString(2);
                String adresa = rs.getString(3);
                int bitkoini = rs.getInt(4);
                
                miners.add(new Rudar(ime, sifra, adresa, bitkoini));
            }
        }
        catch(SQLException n){}
        finally
        {
            Baza_rudara.zatvoriBazu();
            return miners;
        }
    }
}