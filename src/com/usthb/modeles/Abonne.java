package com.usthb.modeles;


import java.io.Serializable;
import java.util.*;

public class Abonne implements Serializable{
    private static int nombreUsers=0;
    public int numero;
    public String nom, prenom;
    private String username, password, specialite, fonction, niveau;
    private MaDate dateDeNaissance;
    private Categorie categorie;
    private ArrayList<Abonne> amis;
    private ArrayList<Publication> publications;
    private ArrayList<Notification> notifications;
    private ArrayList<Invitation> invitations;
    public boolean online;

    public Abonne(String nom,
                  String prenom,
                  String username,
                  String password,
                  String specialite,
                  String fonction,
                  String niveau,
                  MaDate dateDeNaissance,
                  Categorie categorie) {
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
        this.password = password;
        this.specialite = specialite;
        this.fonction = fonction;
        this.niveau = niveau;
        this.dateDeNaissance = dateDeNaissance;
        this.categorie = categorie;
        this.numero = ++nombreUsers;
        this.amis = new ArrayList<>();
        this.publications = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.invitations = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    //i used this method instead of using a getter to compare the password ( measure of security )
    public boolean comparePassword(String password) {
        return (password.equals(this.password));
    }

    public String getSpecialite() {
        return specialite;
    }

    public String getFonction() {
        return fonction;
    }

    public String getNiveau() {
        return niveau;
    }

    public MaDate getDateDeNaissance() {
        return dateDeNaissance;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public ArrayList<Abonne> getAmis() {
        return amis;
    }

    public ArrayList<Publication> getPublications() {
        return publications;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public ArrayList<Invitation> getInvitations() {
        return invitations;
    }

    //this method checks if "this" has a friend with the username "username"
    public boolean aAmi(String username){
        for (Abonne abonne:amis) {
            if(abonne.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    //this method checks if "this" has a friend request from "username"
    public boolean aInvitation(String username){
        for (Invitation invitation:invitations) {
            if(invitation.getUser().equals(username) && invitation.getEtat() == EtatInvitation.instance){
                return true;
            }
        }
        return false;
    }
    //this method returns the reference to the invitation from "username" to "this"
    public Invitation getInvitation(String username){
        for (Invitation invitation:invitations) {
            if(invitation.getUser().equals(username) && invitation.getEtat() == EtatInvitation.instance){
                return invitation;
            }
        }
        return null;
    }

    //this method returns the reference to the publication with the id "id"
    public Publication publicationParId(int id){
        for (Publication publication:publications) {
            if(publication.numero == id){
                return publication;
            }
        }
        return null;
    }

    //method that i'll use while coding the client/server side
    public static void supprimerAbonne(){
        nombreUsers--;
    }

}
