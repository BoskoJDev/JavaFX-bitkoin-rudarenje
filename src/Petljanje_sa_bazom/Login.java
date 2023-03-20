package Petljanje_sa_bazom;

import Meni.PocetniPanel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Bosko
 */
public class Login extends Application
{
    private final TextField user = new TextField();
    private final PasswordField password = new PasswordField();
    private final Button uloguj = new Button("Uloguj se");
    private final Button novo = new Button("Napravi nalog");
    
    //Objekat koji ce cuvati podatke ulogovanog rudara
    public static Rudar rudar = new Rudar();
    
    @Override
    public void start(Stage primaryStage)
    {
        Baza_rudara.initDB();
        
        HBox hb = new HBox(novo);
        hb.setAlignment(Pos.CENTER_RIGHT);
        
        Label name = new Label("Unesite ime:");
        name.setStyle("-fx-text-fill: gold");
        
        Label pass = new Label("Unesite šifru:");
        pass.setStyle("-fx-text-fill: gold");
        
        GridPane root = new GridPane();
        root.add(name, 0, 0);
        root.add(user, 1, 0);
        root.add(pass, 0, 1);
        root.add(password, 1, 1);
        root.add(uloguj, 0, 2);
        root.add(hb, 1, 2);
        root.setStyle("-fx-background-color: linear-gradient(blue, skyblue)");
        
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10, 10, 10, 10));
        
        uloguj.setOnAction(e -> {
            Alert uzbuna = new Alert(AlertType.WARNING);
            uzbuna.setHeaderText("Greška tokom logovanja");
            
            if(user.getText().equals("") || password.getText().equals(""))
            {
                uzbuna.setContentText("Unesi podatke!");
                uzbuna.showAndWait();
            }
            else if(ispravnost(user, password))
            {
                primaryStage.close();
                new PocetniPanel().start(primaryStage);
            }
            else
            {
                uzbuna.setContentText("Nisi uneo(la) adekvatno ime ili šifru!");
                uzbuna.showAndWait();
            }
        });
        uloguj.setStyle("-fx-background-color: linear-gradient(to right, red, yellow);"
        + "-fx-text-fill: white; -fx-background-radius: 6; -fx-font-weight: bold");
        
        novo.setOnAction(e -> {
            primaryStage.close();
            new NoviKorisnik().start(primaryStage);
        });

        novo.setStyle("-fx-background-color: linear-gradient(to left, green, yellow);"
        + "-fx-text-fill: black; -fx-background-radius: 6; -fx-font-weight: bold");
        
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            
            Alert potvrdi = new Alert(AlertType.CONFIRMATION);
            potvrdi.setHeaderText("Sigurno zelite izaci?");
            Optional<ButtonType> confirm = potvrdi.showAndWait();
            
            //Provera da li je rudar kliknuo na 'OK' ili 'Cancel'
            if(confirm.get() == ButtonType.CANCEL)
            {
                potvrdi.close();
            }
            else if(confirm.get() == ButtonType.OK)
            {
                primaryStage.close();
            }
        });
        
        Scene scene = new Scene(root, 250, 100);
        
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    private boolean ispravnost(TextField tf, PasswordField pf)
    {
        boolean nadjen = false;
        Baza_rudara.naredba = "SELECT * FROM `rudari`";
        try
        {
            Statement iskaz = Baza_rudara.veza.createStatement();
            ResultSet rs = iskaz.executeQuery(Baza_rudara.naredba);
        
            while(rs.next())
            {
                if(tf.getText().equals(rs.getString(1)) && pf.getText().equals(rs.getString(2)))
                {
                    rudar.setIme(rs.getString(1));
                    rudar.setSifra(rs.getString(2));
                    rudar.setAdresa(rs.getString(3));
                    rudar.setBroj_bitkoina(rs.getInt(4));
                    nadjen = true;
                }
            }
        }
        catch(SQLException n){System.out.println(nadjen);}
        finally
        {
            return nadjen;
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}