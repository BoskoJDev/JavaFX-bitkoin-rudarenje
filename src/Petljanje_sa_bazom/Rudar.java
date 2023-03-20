package Petljanje_sa_bazom;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Bosko
 */
public class Rudar
{
    private String ime;
    private String sifra;
    private String adresa;
    private int broj_bitkoina;

    public Rudar() {
        this.ime = "";
        this.sifra = "";
        this.adresa = "";
        this.broj_bitkoina = 0;
    }

    public Rudar(String ime, String sifra, String adresa, int broj_bitkoina) {
        this.ime = ime;
        this.sifra = sifra;
        this.adresa = adresa;
        this.broj_bitkoina = broj_bitkoina;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public int getBroj_bitkoina() {
        return broj_bitkoina;
    }

    public void setBroj_bitkoina(int broj_bitkoina) {
        this.broj_bitkoina = broj_bitkoina;
    }
    
    public void generisi_bitkoine(int broj_bitkoina, String adress)
    {
        String sql = "UPDATE `rudari` SET broj_bitkoina = broj_bitkoina + ? WHERE adresa = ?";
        
        Baza_rudara.initDB();
        
        try
        {
            PreparedStatement ps = Baza_rudara.veza.prepareStatement(sql);
            ps.setInt(1, broj_bitkoina);
            ps.setString(2, adress);
            ps.executeUpdate();
            ps.closeOnCompletion();
        }
        catch(SQLException n){}
        finally
        {
            Baza_rudara.zatvoriBazu();
        }
    }
}