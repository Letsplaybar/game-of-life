package de.letsplaybar.gameoflife.game;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Letsplaybar
 * Created on 15.09.2017.
 */
public class SpielFeld {

    private HashMap<Integer, ArrayList<Zelle>> feld; // int = reihe Unser Feld

    private int zug;// Anzahl in welcher Generation man sich befindet

    private boolean end; // schaut ob alle Zellen tod sind

    public SpielFeld(int spalte, int reihe){ //initialisiert das Feld mit der Lenge reihe x spalte
        feld = new HashMap<>();//ertsellt die MAp
        zug =0; // setzt Zug auf 0;
        end = false; // setzt das das Spiel noch nicht zuende ist
        for(int ii =0; ii < reihe;ii++){//erstellt die Zellen für das Feld
            feld.put(ii,new ArrayList<>());
            for(int xx=0; xx < spalte; xx++){
                feld.get(ii).add(new Zelle());
            }
        }
    }

    public void init(HashMap<Integer,ArrayList<Integer>> initValue){ // setzt alle Lebenden Zellen aus der Initmap
        for(int ii : initValue.keySet()){ // Bekomme Reihe
            for (int xx : initValue.get(ii)){ // Bekomme Spalte für Lebende Zellen aus reihe ii
                feld.get(ii).get(xx).changeStatus();// get Zelle aus Reihe ii und Spalte xx und mache sie Lebendig
            }
        }
        printFeld();// Gebe das Startspielfeld aus
    }

    public void zug(){ // führe einen Zug aus
        if(end) // falls spiel zuende beende das Spiel
            return;
        ArrayList<Zelle> change = new ArrayList<>(); // Liste mit Zellen die Ihren Status Lebend oder Tod ändern sollen
        for(int ii : feld.keySet()){
            ArrayList<Zelle> reihe = feld.get(ii);
            for(int xx = 0; xx < reihe.size(); xx++){
                ArrayList<Zelle> nachbarn = new ArrayList<>();// Liste für nachbarn von Zelle [ii,xx]
                if(xx == 0){// falls xx == 0 1.Sonderfall rand
                    //selben
                    nachbarn.add(reihe.get(reihe.size()-1));//füge nachbarn aus selben reihe hinzu
                    nachbarn.add(reihe.get(xx+1));
                    ArrayList<Zelle> reihevor;// reihe höher
                    if(ii ==0){//sonderfall falls reihe 0 Rand wechseln
                        reihevor = feld.get(feld.keySet().size()-1);
                    }else{
                        reihevor = feld.get(ii-1);
                    }
                    ArrayList<Zelle> reihenach; // reihe drunter
                    if(ii == feld.keySet().size()-1){ // Sonderfall falls Reihe max dann Seite Wechseln
                        reihenach = feld.get(0);
                    }else{
                        reihenach = feld.get(ii+1);
                    }
                    //obereb
                    nachbarn.add(reihevor.get(xx));//füge Nachbarn aus oberer Reihe hinzu
                    nachbarn.add(reihevor.get(xx+1));
                    nachbarn.add(reihevor.get(reihevor.size()-1));
                    // unteren
                    nachbarn.add(reihenach.get(xx));//füge Nachbarn aus unterer Reihe hinzu
                    nachbarn.add(reihenach.get(xx+1));
                    nachbarn.add(reihenach.get(reihenach.size()-1));
                }else if(xx == reihe.size()-1){ // 2. Sonderfall xx == Max spalte wechsle Seite bei nachbarn
                    //selben
                    nachbarn.add(reihe.get(xx-1));//füge Nachbarn aus selber Reihe hinzu
                    nachbarn.add(reihe.get(0));
                    ArrayList<Zelle> reihevor; // reihe vor
                    if(ii ==0){ // Sonderfall fals ii Reihe 0 ist
                        reihevor = feld.get(feld.keySet().size()-1);
                    }else{
                        reihevor = feld.get(ii-1);
                    }
                    ArrayList<Zelle> reihenach; // reihe für danach
                    if(ii == feld.keySet().size()-1){ // sonderfall falls ii = max Reihe ist
                        reihenach = feld.get(0);
                    }else{
                        reihenach = feld.get(ii+1);
                    }
                    //obereb
                    nachbarn.add(reihevor.get(xx));//füge  die Nachbarn aus Reihe davor hinzu
                    nachbarn.add(reihevor.get(xx-1));
                    nachbarn.add(reihevor.get(0));
                    // unteren
                    nachbarn.add(reihenach.get(xx));///füge die NAchbarn aus Reihe nach hinzu
                    nachbarn.add(reihenach.get(xx-1));
                    nachbarn.add(reihenach.get(0));
                }else{//standartfall
                    //selben
                    nachbarn.add(reihe.get(xx-1));// füge NAchbarn aus selben Reihe hinzu
                    nachbarn.add(reihe.get(xx+1));
                    ArrayList<Zelle> reihevor; // reihe davor
                    if(ii ==0){ // Sonderfall ii == 0 Seitenwechsel
                        reihevor = feld.get(feld.keySet().size()-1);
                    }else{
                        reihevor = feld.get(ii-1);
                    }
                    ArrayList<Zelle> reihenach; // reihe danach
                    if(ii == feld.keySet().size()-1){//SOnderfall fals ii == Max Reihe Seitenwechsel
                        reihenach = feld.get(0);
                    }else{
                        reihenach = feld.get(ii+1);
                    }
                    //obereb
                    nachbarn.add(reihevor.get(xx));//füge Nachbarn hinzu für Reihe vor
                    nachbarn.add(reihevor.get(xx-1));
                    nachbarn.add(reihevor.get(xx+1));
                    // unteren
                    nachbarn.add(reihenach.get(xx));//füge Nachbarn hinzu für Reihe nach
                    nachbarn.add(reihenach.get(xx-1));
                    nachbarn.add(reihenach.get(xx+1));
                }

                int amount=0; // Anzahl der lebenden Nachbarn

                for(Zelle zelle : nachbarn) // zähle die lebendne nachbarn durch
                    if (zelle.isStatus())
                        amount++;
                Zelle zelle = reihe.get(xx);// Bekomme Zelle [ii,xx]
                if(zelle.isStatus()){ // schaue ob zelle Lebt
                    if(amount != 2 && amount != 3) // schaue ob vorausetzung zum sterbern gegeben ist
                        change.add(zelle); // füge si zur ändern liste hinzu
                }else{// ist sie tod
                    if(amount == 3){//schau ob bedingungen zum Lebendig werden gegeben sind
                        change.add(zelle); // füge sie in die änderungsliste ein
                    }
                }

            }
        }
        for (Zelle zelle: change) // gehe zu jeder Zelle die sich  ändern muss und wechsle sie von Lebendig zu Tod und von Tod zu Lebendig
            zelle.changeStatus();
        printFeld(); // Gebe den Stand des Feldes Aus
        checkEnd();//schaue ob Spiel zuende ist d.h. wir uns in der Letzen Generatin Befinden
    }

    private void printFeld(){ // Gebe Feld wieder
        System.out.println(); // erstelle eine Leere Zeile
        System.out.println("Generation: "+zug++); // Gebe wieder in welcher Generation wir uns befindne
        for(int ii : feld.keySet()){ // Gehe die Reihen ab
            for(Zelle zelle: feld.get(ii)){//Gehe die Spalten der Reihe ii ab
                System.out.print((zelle.isStatus()?"■":"□"));// Gebe ein Leeres Feld für Tote Zellen wieder und für Lebende eine Schwarzes Feld
            }
            System.out.println();// Gehe für jede Neue Reihe in eine Neue Reihe
        }
    }

    private void checkEnd(){
        for(int ii : feld.keySet()){ // Schaue ob es noch lebende Zellen gibt
            for(Zelle zelle: feld.get(ii)){
                if(zelle.isStatus())
                    return;
            }
        }// falls nicht setze end auf True und gebe aus das das Spiel Zuende ist
        end = true;
        System.out.println("Das Spiel Ist zu Ende es hat keine Zelle Überlebt");
    }


}
