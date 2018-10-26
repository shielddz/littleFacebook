package com.usthb.modeles;

import java.io.Serializable;
import java.util.ArrayList;

public class Publication implements Serializable {
    private static int nombrePublications = 0;
    public int numero;
    private String contenu;
    private niveauVisibilitePublication niveau;
    public int nombrePartages;
    public Publication publicationMere=null;
    private boolean epinglee;
    //used it to display the name of the owner in the controllers
    public Abonne abonnePere = null;
    public ArrayList<Commentaire> commentaires;
    public ArrayList<Reaction> reactions;

    public Publication(String contenu, niveauVisibilitePublication niveau, boolean epinglee) {
        this.contenu = contenu;
        this.niveau = niveau;
        this.epinglee = epinglee;
        this.numero = ++nombrePublications;
        this.nombrePartages = 0;
        this.commentaires = new ArrayList<>();
        this.reactions = new ArrayList<>();
    }
    //used to do the share feature
    public Publication(String contenu, niveauVisibilitePublication niveau, boolean epinglee, Abonne abonnePere) {
        this.contenu = contenu;
        this.niveau = niveau;
        this.epinglee = epinglee;
        this.numero = ++nombrePublications;
        this.nombrePartages = 0;
        this.commentaires = new ArrayList<>();
        this.reactions = new ArrayList<>();
        this.abonnePere = abonnePere;
        this.publicationMere = publicationMere;
    }
    public Publication(String contenu, niveauVisibilitePublication niveau){
        this(contenu, niveau, false);
    }
    public Publication(Publication publication){
        this.contenu = publication.contenu;
        this.niveau = publication.niveau;
        this.epinglee = publication.epinglee;
        this.numero = ++nombrePublications;
        this.nombrePartages = 0;
        this.commentaires = new ArrayList<>();
        this.reactions = new ArrayList<>();
    }
    public String getContenu() {
        return contenu;
    }

    public ArrayList<Commentaire> getCommentaires() {
        return commentaires;
    }

    public niveauVisibilitePublication getNiveau() {
        return niveau;
    }

    public boolean isEpinglee() {
        return epinglee;
    }

    public void setEpinglee(boolean epinglee) {
        this.epinglee = epinglee;
    }

    //get the reaction of "this" with the username "username" ( aka get the reaction that a certain user left on this post )
    public Reaction usernameReaction(String username){
        for (Reaction reaction:reactions) {
            if(reaction.username.equals(username)){
                return reaction;
            }
        }
        return null;
    }

    //if the user changes his reaction ( overwrites it ), then i had to find some way to delete the old one
    //and that's where this method's utility comes in.
    public void supprimerReactionUsername(Reaction reaction){
        if(reaction != null) {
            if (this.reactions.contains(reaction)) {
                this.reactions.remove(reaction);
            }
        }
    }


}
