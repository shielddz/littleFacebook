package com.usthb.modeles;

import java.io.Serializable;

public class Commentaire implements Serializable{
    private String commentaire;
    //save the user that connected, to use it in the display of the comment ( helps us in the controller to track the user that commented )
    public Abonne user;

    public Commentaire(String commentaire, Abonne user) {
        this.commentaire = commentaire;
        this.user = user;
    }

    public String getCommentaire() {
        return commentaire;
    }
}