package Klijent_server;

import Petljanje_sa_bazom.Login;
import Petljanje_sa_bazom.Baza_rudara;
import Petljanje_sa_bazom.Rudar;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Bosko
 */
public class Admin extends Application
{   
    @Override
    public void start(Stage primaryStage)
    {
        TextField resenje = new TextField();
        resenje.setPrefColumnCount(6);
        resenje.setEditable(false);
        
        TextField odgovor = new TextField();
        odgovor.setPrefColumnCount(6);
        odgovor.setEditable(false);
        
        Button generisi = new Button("Generiši bitkoine");
        generisi.setStyle("-fx-background-color: linear-gradient(to top, blue, lightskyblue);"
        + "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10");
        
        Button netacno = new Button("Netačno");
        netacno.setStyle("-fx-background-color: linear-gradient(#dc9656, #ab4642)");
        
        TableColumn<Rudar, String> ime = new TableColumn<>("Ime rudara");
        TableColumn<Rudar, String> sifra = new TableColumn<>("Šifra rudara");
        TableColumn<Rudar, String> adresa = new TableColumn<>("Adresa rudara");
        TableColumn<Rudar, Integer> bitkoini = new TableColumn<>("Stanje");
        
        ime.setCellValueFactory(new PropertyValueFactory<>("ime"));
        ime.setMinWidth(150);
        sifra.setCellValueFactory(new PropertyValueFactory<>("sifra"));
        sifra.setMinWidth(130);
        adresa.setCellValueFactory(new PropertyValueFactory<>("adresa"));
        adresa.setMinWidth(150);
        bitkoini.setCellValueFactory(new PropertyValueFactory<>("broj_bitkoina"));
        bitkoini.setMinWidth(130);
        
        TableView<Rudar> tabelaRudara = new TableView<>();
        tabelaRudara.getColumns().addAll(ime, sifra, adresa, bitkoini);
        tabelaRudara.setMinSize(200, 150);
        
        ObservableList<Rudar> value = Baza_rudara.vrati_rudare();
        tabelaRudara.setItems(value);
        
        Label solutionLabel = new Label("Rešenje postavljenog pitanja:");
        solutionLabel.setStyle("-fx-font-weight: bold");
        
        HBox solution = new HBox(10, solutionLabel, resenje);
        solution.setAlignment(Pos.CENTER);
        
        Label answerLabel = new Label("Korisnikov odgovor:");
        answerLabel.setStyle("-fx-font-weight: bold");
        
        HBox answer = new HBox(20, answerLabel, odgovor);
        answer.setAlignment(Pos.CENTER);
        
        VBox provera = new VBox(solution, answer);
        provera.setSpacing(3);
        
        VBox dugmici = new VBox(20);
        dugmici.getChildren().addAll(generisi, netacno);
        dugmici.setAlignment(Pos.CENTER);
        
        VBox tata = new VBox(100, provera, dugmici);
        
        Label labela = new Label("Tabela rudara:");
        labela.setStyle("-fx-font-weight: bold");
        
        VBox tabela = new VBox(2, labela, tabelaRudara);
        tabela.setAlignment(Pos.CENTER);
        
        GridPane gp = new GridPane();
        gp.add(tata, 0, 0);
        gp.add(tabela, 1, 0);
        gp.setPadding(new Insets(10, 10, 10, 10));
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setStyle("-fx-background-color: linear-gradient(chartreuse, darkorange)");
        
        Scene scena = new Scene(gp, 840, 450);
        
        primaryStage.setTitle("Admin");
        primaryStage.setScene(scena);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        new Thread(() -> {
            try
            {
                ServerSocket server = new ServerSocket(5000);
                Socket klijent = server.accept();
                JOptionPane.showMessageDialog(null, "Klijent konektovan!");

                DataInputStream input = new DataInputStream(klijent.getInputStream());
                DataOutputStream output = new DataOutputStream(klijent.getOutputStream());
                
                resenje.setText(Miner.objective(resenja().get(Miner.nasumicno)));
                
                odgovor.appendText("" + Integer.parseInt(input.readUTF()));
                
                //Programiranje dogadjaja za dugmice
                generisi.setOnAction(e -> {
                    Login.rudar.generisi_bitkoine(2, Login.rudar.getAdresa());
                    JOptionPane.showMessageDialog(null, "Bitkoini uspesno prosledjeni rudaru!",
                    "", JOptionPane.INFORMATION_MESSAGE);
                    
                    primaryStage.close();
                    Miner.zatvori();
                });
                
                netacno.setOnAction(e -> {
                    Miner.zatvori();
                    primaryStage.close();
                });
                
                if(primaryStage.isShowing())
                {
                    server.close();
                    klijent.close();
                    input.close();
                    output.close();
                }
            }
            catch(IOException j){}
        }).start();
    }
    
    private List<String> resenja()
    {
        List<String> resenja = new ArrayList<>();
        
        resenja.add("resenje1.bin");
        resenja.add("resenje2.bin");
        resenja.add("resenje3.bin");
        resenja.add("resenje4.bin");
        
        return resenja;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}