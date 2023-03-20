package Meni;

import Nit.Bitkoin_Nit;
import Klijent_server.Admin;
import Klijent_server.Miner;
import Petljanje_sa_bazom.Login;
import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Bosko
 */
public class PocetniPanel extends Application
{
    @Override
    public void start(Stage primaryStage)
    {        
        Text naslov = new Text("DOBRO DOŠAO, RUDARU!");
        naslov.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, FontPosture.REGULAR, 20));
        naslov.setFill(Color.GOLD);
        naslov.setStroke(Color.BLACK);
        
        Bitkoin_Nit bitkoin_nit = new Bitkoin_Nit();
        Thread nit = new Thread(bitkoin_nit);
        nit.start();
        
        Label vrednost = new Label(bitkoin_nit.getPrikaz());
        vrednost.setStyle("-fx-background-color: white");

        HBox thread = new HBox(vrednost);
        thread.setAlignment(Pos.TOP_RIGHT);
        
        HBox title = new HBox(naslov);
        title.setAlignment(Pos.BASELINE_CENTER);
        
        VBox titleAndThread = new VBox(10);
        titleAndThread.getChildren().addAll(thread, title);
        
        MenuItem izadji = new MenuItem("Odjavi se");
        izadji.setStyle("-fx-background-color: aqua");
        
        MenuItem kontakt = new MenuItem("Kontaktiraj\nadmina");
        kontakt.setStyle("-fx-background-color: aqua");
        
        Menu menu = new Menu("Zadatak");
        menu.getItems().add(kontakt);
        
        izadji.setOnAction(e -> {
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
                new Login().start(primaryStage);
            }
        });
        
        kontakt.setOnAction(e -> {
            primaryStage.close();
            new Admin().start(new Stage());
            new Miner().start(new Stage());
        });
        
        Menu nalog = new Menu("Nalog");
        nalog.getItems().add(izadji);
        
        MenuBar bar = new MenuBar(menu, nalog);
        bar.setStyle("-fx-background-color: aqua");

        BorderPane root = new BorderPane();
        root.setTop(bar);
        root.setCenter(titleAndThread);
        //Ubacujem sliku
        root.setStyle("-fx-background-image: url(https://s.thestreet.com/files/tsc/v2008/photos/contrib/uploads/9b8b99da-76cd-11e7-955e-9516e7c707a6.png)");
        
        Scene scene = new Scene(root, 580, 380);
        
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        
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
    }
    
    public static void main(String[] args)
    {
        launch(args);
    }
}