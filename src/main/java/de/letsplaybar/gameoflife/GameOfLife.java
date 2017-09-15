package de.letsplaybar.gameoflife;

import de.letsplaybar.gameoflife.game.SpielFeld;
import de.letsplaybar.gameoflife.gui.GUI;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Letsplaybar
 * Created on 15.09.2017.
 */
public class GameOfLife {

    private static  @Getter GameOfLife instance; // Instance von der Main Klasse
    private @Getter @Setter SpielFeld game; // Aktuelles Spielfeld

    public GameOfLife(){
        instance = this; // instane initen
        GUI gui = new GUI();// Gui erstellen
        try {
            gui.start(); // Gui starten
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new GameOfLife();//Start des Programmes
    }
}
