package Petljanje_sa_bazom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Bosko
 */
public class NoviKorisnik extends Application
{
    private final TextField user = new TextField();
    private final TextField adress = new TextField();
    private final PasswordField password = new PasswordField();
    private final PasswordField passwordConfirm = new PasswordField();
    private final Button upisi = new Button("Upisite se");
    private final Button adresa = new Button("Generisi adresu");
    
    @Override
    public void start(Stage primaryStage)
    {
        Label name = new Label("Unesite ime:");
        name.setStyle("-fx-text-fill: gold");
        
        Label pass = new Label("Unesite šifru:");
        pass.setStyle("-fx-text-fill: gold");
        
        Label confirm = new Label("Potvrdite šifru:");
        confirm.setStyle("-fx-text-fill: gold");
        
        adresa.setOnAction(e -> {
            adress.setText(generisanjeAdrese().toString()); //OSTAVI OVAKO!!
            adress.setEditable(false);
            adresa.setDisable(true);
        });
        adresa.setStyle("-fx-background-color: linear-gradient(to bottom, crimson, chocolate);"
        + "-fx-text-fill: gold");
        
        upisi.setOnAction(e -> {
            if(dodaj())
            {
                primaryStage.close();
                new Login().start(primaryStage);
            }
        });
        upisi.setStyle("-fx-background-color: linear-gradient(yellowgreen, green, lightgreen)");
        
        GridPane root = new GridPane();
        root.add(name, 0, 0);
        root.add(user, 1, 0);
        root.add(pass, 0, 1);
        root.add(password, 1, 1);
        root.add(confirm, 0, 2);
        root.add(passwordConfirm, 1, 2);
        root.add(adresa, 0, 3);
        root.add(adress, 1, 3);
        root.add(upisi, 1, 5);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10, 5, 10, 5));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, lightskyblue, blue)");
        GridPane.setHalignment(upisi, HPos.RIGHT);
        
        Scene scene = new Scene(root, 270, 210);
        
        primaryStage.setResizable(false);
        primaryStage.setTitle("Novi nalog");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            
            Alert potvrdi = new Alert(AlertType.CONFIRMATION);
            potvrdi.setHeaderText("Sigurno zelite izaci?");
            Optional<ButtonType> pot = potvrdi.showAndWait();
            
            //Provera da li je rudar kliknuo na 'OK' ili 'Cancel'
            if(pot.get() == ButtonType.CANCEL)
            {
                potvrdi.close();
            }
            else if(pot.get() == ButtonType.OK)
            {
                primaryStage.close();
            }
        });
    }
    
    private boolean dodaj()
    {
        Baza_rudara.initDB();
        
        Rudar miner = new Rudar();
        boolean validno = false;
        
        Baza_rudara.naredba = "INSERT INTO `rudari`(`ime`, `sifra`, `adresa`, `broj_bitkoina`) VALUES (?, ?, ?, '0')";
        miner.setIme(user.getText());
        miner.setSifra(password.getText());
        String potvrda = passwordConfirm.getText();
        miner.setAdresa(adress.getText());
        
        try
        {
            if(miner.getIme().equals("") || miner.getIme().equals("") || potvrda.equals("") || miner.getAdresa().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Unesite podatke!");
            }
            else if(!password.getText().equals(passwordConfirm.getText()))
            {
                JOptionPane.showMessageDialog(null, "Sifre se ne podudaraju!");
            }
            else
            {
                PreparedStatement ps = Baza_rudara.veza.prepareStatement(Baza_rudara.naredba);
                ps.setString(1, miner.getIme());
                ps.setString(2, miner.getSifra());
                ps.setString(3, miner.getAdresa());
                ps.executeUpdate();

                validno = true;
            }
        }
        catch(SQLException j)
        {
            JOptionPane.showMessageDialog(null, "Greska pri radu sa bazom!", "",
            JOptionPane.ERROR_MESSAGE);
            Platform.exit();
        }
        finally
        {
            Baza_rudara.zatvoriBazu();
            return validno;
        }
    }
    
    private char[] generisanjeAdrese()
    {
        Random r;
        char[] bitcoin_adress = new char[11];
        for(int i = 0; i < bitcoin_adress.length; i++)
        {
            r = new Random();
            bitcoin_adress[i] = (char) (97 + r.nextInt() % 26);
        }
        
        return bitcoin_adress;
    }

    public static void main(String[] args) {
        launch(args);
    }
}