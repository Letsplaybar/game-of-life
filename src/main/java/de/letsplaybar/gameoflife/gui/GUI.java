package de.letsplaybar.gameoflife.gui;/**
 * @author Letsplaybar
 * Created on 15.09.2017.
 */

import de.letsplaybar.gameoflife.GameOfLife;
import de.letsplaybar.gameoflife.game.SpielFeld;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class GUI extends Application {

    private Stage stage;
    private TextArea game;


    public void start(){
        launch(); // staren des Fensters
    }

    @Override
    public void start(Stage primaryStage) {
        game = new TextArea(); // erstelle Textarea zum anzeigen der Generations
        game.setFont(Font.font("Arial",12)); // Schrieftartsetzen
        game.setMinSize(720,400); // Größe setzen
        game.setEditable(false);//nicht editierbar machen
        PrintStream ps = new PrintStream(System.out){ // Init Für Umleitung des Outputstreams System.out
            @Override
            public void println(String s) {
                print(s+"\n"); // rufe print auf mit \n
            }

            @Override
            public void println() {
                super.println();
                game.setText(game.getText()+"\n"); // füge den Text ran
                game.selectRange(game.getText().length(),game.getText().length()); // sorgt dafür das das Textarea unten ist
            }

            @Override
            public void print(String s) {
                super.print(s);
                game.setText(game.getText()+s); // füge Text ran
                game.selectRange(game.getText().length(),game.getText().length()); // sorgt dafür das das Textarea unten ist
            }
        };
        System.setOut(ps); // setze den Neuen Outputstream
        stage = primaryStage;
        stage.setTitle("Game of Life"); //Titel des Fensters setzen
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/Logo.png"))); //Logo setzen
        stage.centerOnScreen(); // Startposition auf die Mitte setzen
        openInit(); // init Scene setzen
    }

    private void openInit(){
        Pane pane = new VBox();// Init
        Text error = new Text();
        TextField reihe = new TextField();
        TextField spalte = new TextField();
        Button button = new Button("OK");
        button.setOnAction(new EventHandler<ActionEvent>() { // Weitermachen beim Klick auf den Button
            @Override
            public void handle(ActionEvent event) {
                String r = reihe.getText(); //Text holen
                String s = spalte.getText();//Text holen
                try{
                    int reihe = Integer.parseInt(r); //zur Zahl parsen
                    int spalte = Integer.parseInt(s);//zur Zahl parsen
                    GameOfLife.getInstance().setGame(new SpielFeld(spalte,reihe)); // Game of Life Feld erstellen
                    openSetting(reihe,spalte); // Setting öffnen für lebene Zellen
                }catch (NumberFormatException ex){
                    error.setText("Zeile und Reihe müssen Ganze Zahlen sein");//Gebe fehler aus
                }
            }
        });
        pane.getChildren().add(error); //füge Elemente hinzu
        pane.getChildren().addAll(new Text("Reihe:"),reihe);
        pane.getChildren().addAll(new Text("Spalte:"),spalte);
        pane.getChildren().add(button);
        Scene scene = new Scene(pane,720,520); //Scene erstelleb
        stage.setScene(scene); //scene setzen
        stage.show(); //Zeige Fenster
    }

    private void openSetting(int reihe, int spalte){
        final ArrayList<TextField> fiel = new ArrayList<>();// Liste mit allen Textfields
        Text error = new Text(); // error Text
        Button button = new Button("OK"); // Button für Bestaätigung
        button.setOnAction(new EventHandler<ActionEvent>() { // Event für klick auf Button
            @Override
            public void handle(ActionEvent event) {
                HashMap<Integer, ArrayList<Integer>> map = new HashMap<>(); // map für init
                for(int ii =0; ii<reihe; ii++){ // gehe Reihen ab
                    if(fiel.get(ii).getText().equalsIgnoreCase("")){ // wenn Feld leer überspringe reihe da keins lebt
                        continue;
                    }
                    if(!fiel.get(ii).getText().contains(",")){ // Wenn nur eine Lebene Zelle füre das aus
                        ArrayList<Integer> reihe = new ArrayList<>(); // Liste für Reihe
                        try{
                            int zell = Integer.parseInt(fiel.get(ii).getText()); // Bekomme Zahl aus String
                            if(zell < spalte){ // ist Zahl kleiner als Spalte
                                reihe.add(zell); // Füge zahl hinzu
                            }else{ // sonst
                                error.setText("Die Zahl darf nur max "+(spalte-1)+" groß sein!"); //gebe error aus
                            }
                        }catch (NumberFormatException ex){
                            error.setText("zwischen den , dürfen nur Ganze Zahlen Stehen"); // gebe Fehler aus wenn keine Zahl
                            return;
                        }
                        map.put(ii,reihe); // füge liste zur initmap
                        continue;// gehe zur nächsten reihe
                    }
                    String[] zellen = fiel.get(ii).getText().split(","); // Stringarray mit Lebenden Zellen
                    ArrayList<Integer> reihe = new ArrayList<>(); // Liste für reihe
                    for(String zelle : zellen){ // für alle Zellen in Reihe
                        try{
                            int zell = Integer.parseInt(zelle); // mache String zu int
                            if(zell < spalte){ // wenn Zelle kleiner als spalte
                                reihe.add(zell); // füge Zelle hinzu
                            }else{ //sonst
                                error.setText("Die Zahl darf nur max "+(spalte-1)+" groß sein!"); // gebe fehler aus
                            }
                        }catch (NumberFormatException ex){
                            error.setText("zwischen den , dürfen nur Ganze Zahlen Stehen"); //gebe fehler aus
                            return;
                        }
                    }
                    map.put(ii,reihe); // pack reihe in map
                }
                GameOfLife.getInstance().getGame().init(map); // inite die Map damit alle Zellen ausgangsposition haben
                openGame(); // öffne das Spiel
            }
        });
        Pane pane = new VBox();//füge elemente zur Pane
        pane.getChildren().add(new Text("Gebe hier die Zellen an die am Anfang Leben sollen! [0:"+(spalte-1)+"]"));
        pane.getChildren().add(error);
        for(int ii =0;ii< reihe;ii++){ // erstelle pro Reihe 1 Textfield und füge ihn zum Pane
            TextField field = new TextField("0,...,"+(spalte-1));
            pane.getChildren().addAll(new Text("Reihe "+ii+".:"),field);
            fiel.add(field);
        }
        pane.getChildren().add(button);
        Scene scene = new Scene(pane,720,520); // Scene erstellen
        stage.setScene(scene); // scene setzen
        stage.show(); // Fenster aktualisieren
    }

    private void openGame(){
        Button button = new Button("nextGeneration");//Button um die Nächste Generation zu bekommen
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameOfLife.getInstance().getGame().zug(); // führe den nächsten Zug aus
            }
        });
        Button newGame = new Button("neues Spiel");// Button um ein Neues Spiel zu erstellen
        newGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openInit();// öffne Init Fenster
            }
        });
        Pane pane = new VBox(); // pane erstellen
        pane.getChildren().add(game); // GameFenster hinzufügen um die Konsoleneingabe zu sehen
        HBox hbox = new HBox(); // Button box
        hbox.getChildren().addAll(button,newGame); // füge button ein
        pane.getChildren().add(hbox);
        Scene scene = new Scene(pane,720,520); // Scene erstellen
        stage.setScene(scene); // scene setzen
        stage.show(); // Fenster Aktuallisieren
    }

}
