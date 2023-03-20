package Klijent_server;

import Meni.PocetniPanel;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Bosko
 */
public class Miner extends Application
{
    private static Stage prozor = new Stage();
    private Random random = new Random();
    protected static int nasumicno;
    
    @Override
    public void start(Stage primaryStage)
    {
        //Potrebne komponente
        primaryStage = prozor;
        TextField odgovor = new TextField();
        
        TextArea zadatak = new TextArea();
        zadatak.setPrefRowCount(2);
        zadatak.setEditable(false);
        
        TextArea resavanje = new TextArea();
        resavanje.setPrefRowCount(10);
        
        Label postupakLabel = new Label("Postupak:");
        postupakLabel.setStyle("-fx-font-weight: bold");
        
        VBox vb = new VBox(10, zadatak, postupakLabel, resavanje);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(10, 10, 10, 10));
        
        Label odgovorLabel = new Label("Pošalji odgovor:");
        odgovorLabel.setStyle("-fx-font-weight: bold");
        
        HBox hb = new HBox(10, odgovorLabel, odgovor);
        hb.setAlignment(Pos.CENTER);
        hb.setPadding(new Insets(10, 10, 10, 10));
        
        BorderPane root = new BorderPane();
        root.setCenter(vb);
        root.setBottom(hb);
        root.setStyle("-fx-background-color: linear-gradient(chartreuse, gold, yellow)");
        
        Scene scene = new Scene(root, 450, 415);
        
        prozor.setResizable(false);
        prozor.setTitle("Zadatak");
        prozor.setScene(scene);
        prozor.show();
        
        try
        {
            Socket klijent = new Socket("localhost", 5000);

            DataOutputStream output = new DataOutputStream(klijent.getOutputStream());
            
            odgovor.setOnKeyPressed(e -> {
                //Proveravam da li je rudar pritisnuo 'ENTER'
                if(e.getCode() == KeyCode.ENTER)
                {
                    try { output.writeUTF(odgovor.getText()); }
                    catch(IOException ex)
                    {
                        JOptionPane.showMessageDialog(null, "Greška sa tokom!",
                        "", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            
            nasumicno = random.nextInt(4);
            zadatak.appendText(Miner.objective(zadaci().get(Math.abs(nasumicno))));
        }
        catch(IOException j){}
        
        prozor.setOnCloseRequest(e -> {
            e.consume();
            
            Alert potvrdi = new Alert(Alert.AlertType.CONFIRMATION);
            potvrdi.setHeaderText("Sigurno zelite izaci?");
            Optional<ButtonType> confirm = potvrdi.showAndWait();
            
            //Provera da li je rudar kliknuo na 'OK' ili 'Cancel'
            if(confirm.get() == ButtonType.CANCEL)
            {
                potvrdi.close();
            }
            else if(confirm.get() == ButtonType.OK)
            {
                prozor.close();
            }
        });
    }
    
    protected static void zatvori()
    {
        prozor.close();
        new PocetniPanel().start(prozor);
    }
    
    private List<String> zadaci()
    {
        List<String> zadaci = new ArrayList<>();
        
        zadaci.add("zadatak1.bin");
        zadaci.add("zadatak2.bin");
        zadaci.add("zadatak3.bin");
        zadaci.add("zadatak4.bin");
        
        return zadaci;
    }
    
    protected static String objective(String filename)
    {
        String s;
        
        try
        {            
            File f2 = new File(filename);
            BufferedReader is = new BufferedReader(new FileReader(f2));
            s = is.readLine();
            return s;
        }
        catch(IOException j)
        {
            System.out.println("lkjfdsf");
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}