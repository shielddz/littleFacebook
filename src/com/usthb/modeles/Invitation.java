package com.usthb.modeles;

import java.io.Serializable;

public class Invitation implements Serializable{
    private String user;
    public String nom;
    public String prenom;
    private String message;
    private EtatInvitation etat;

    public Invitation(String user, String message) {
        this.user = user;
        this.message = message;
        this.etat = EtatInvitation.instance;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public EtatInvitation getEtat() {
        return etat;
    }

    public void setEtat(EtatInvitation etat) {
        this.etat = etat;
    }
}
