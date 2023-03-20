package Nit;

import java.io.IOException;
import java.net.UnknownHostException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Bosko
 */
public final class Bitkoin_Nit implements Runnable
{
    private String prikaz;
    
    @Override
    public void run()
    {
        try
        {
            Document doc = Jsoup.connect("https://coincap.io/").get();
            Elements elementi = doc.select(".table tr td span");
            prikaz = "Trenutna vrednost bitkoina:\n" + elementi.get(0).text();
        }
        catch(IOException ex){ ex.printStackTrace(); }
    }
    public Bitkoin_Nit() {
        run();
    }

    public String getPrikaz() {
        return this.prikaz;
    }
}