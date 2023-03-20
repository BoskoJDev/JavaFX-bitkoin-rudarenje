package Petljanje_sa_bazom;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Bosko
 */
public class RudarTest {
    
    public RudarTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testGenerisi_bitkoine()
    {
        System.out.println("generisi_bitkoine");
        int broj_bitkoina = 1;
        String adress = "";
        Rudar instance = new Rudar();
        instance.generisi_bitkoine(broj_bitkoina, adress);
        assertTrue(broj_bitkoina > 0);
    }
}