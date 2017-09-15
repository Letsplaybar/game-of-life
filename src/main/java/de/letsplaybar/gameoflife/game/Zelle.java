package de.letsplaybar.gameoflife.game;

import lombok.Getter;

/**
 * @author Letsplaybar
 * Created on 15.09.2017.
 */
public class Zelle {

    private @Getter boolean status; // bestimmt ob Zelle Lebendig oder tot ist inkl. Getter

    public Zelle(){
        status = false;// staus standart tot
    }

    public void changeStatus(){// Staus der Zelle wechseln
        status = !status;
    }


}
