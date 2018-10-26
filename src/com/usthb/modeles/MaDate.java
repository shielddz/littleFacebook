//created this class of mine, because i had trouble dealing with the JAVA Date class.
package com.usthb.modeles;

import java.io.Serializable;

public class MaDate implements Serializable{
    public int jour, mois, année;

    public MaDate(int jour, int mois, int année) {
        this.jour = jour;
        this.mois = mois;
        this.année = année;
    }

    public MaDate(String date) {
        this.jour = this.conversionDate(date, "jj");
        this.mois = this.conversionDate(date, "mm");
        this.année = this.conversionDate(date, "yyyy");
    }

    //what == jj or mm or yyyy
    public int conversionDate(String date, String what){
        switch(what){
            case "jj":
                return Integer.valueOf(date.substring(0, 2));
            case "mm":
                return Integer.valueOf(date.substring(3, 5));
            case "yyyy":
                return Integer.valueOf(date.substring(6,10));
            default :
                return -1;
        }
    }

    @Override
    public String toString() {
        return jour+"/"+mois+"/"+année;
    }
}
