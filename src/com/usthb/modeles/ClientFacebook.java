package com.usthb.modeles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ClientFacebook extends Application {
    //temporary variables that i'll use mostly to create pages ( i'll use them in the controllers )
    public static Publication publicationTmp=null;
    public static Commentaire commentTmp=null;
    public static Abonne abonneTmp=null;
    public static Invitation invitationTmp=null;
    public static Notification notificationTmp=null;
    public static int numberNotifications=0;
    public static int numberInvitations=0;
    //end of temporary variables.

    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    //current user refers to the connected user object
    //current view refers to the currently viewed user ( searched or == current user initially )
    public static Abonne currentUser = null;
    public static Abonne currentView = null;

    private static boolean connected = false;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1337);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        launch(args);
        socket.close();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent login = FXMLLoader.load(getClass().getResource("FXMLs/Login.fxml"));
        primaryStage.setTitle("login.");
        primaryStage.setScene(new Scene(login));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e->quit());
    }

    //update is set to update the currentUser and currentView after a post, or to refresh the wallPane ( used in indexController )
    public static void update() throws IOException, ClassNotFoundException {
        out.writeObject("update "+currentUser.getUsername());
        out.flush();
        currentUser = (Abonne) in.readObject();
        out.writeObject("update "+currentView.getUsername());
        out.flush();
        currentView = (Abonne) in.readObject();
    }
    public static boolean connect(String username,String password) throws IOException, ClassNotFoundException {
        out.writeObject("connect "+username+" "+password);
        out.flush();
        currentUser = (Abonne) in.readObject();
        currentView = currentUser;
        if(currentUser == null){
            connected = false;
            return false;
        }
        connected = true;
        return true;
    }
    public static boolean signUp(Abonne abonne) throws IOException {
        out.writeObject("signup");
        out.flush();
        out.reset();
        out.writeObject(abonne);
        out.flush();
        boolean check = in.readBoolean();
        return check;
    }
    //if the user decides to close the window, we have to handle that case or else the server will shutdown because of
    //an error, since the socket won't get closed.
    public static void quit() {
        try {
            if(connected) {
                out.writeObject("disconnect " + currentUser.getUsername());
                out.flush();
            }
            out.writeObject("quit");
            out.flush();
        } catch (IOException e) {
            System.out.println("Error.");
        }
    }
    public static void disconnect(){
        boolean check = false;
        try {
            out.writeObject("disconnect "+currentUser.getUsername());
            out.flush();
            check = in.readBoolean();
            if(check){
                currentUser = null;
                connected = false;
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
    }
    public static void publier(Publication publication) throws IOException {
        out.writeObject("post "+currentUser.getUsername());
        out.flush();
        out.reset();
        out.writeObject(publication);
        out.flush();
    }
    public static boolean rechercher(String username) throws IOException, ClassNotFoundException {
        out.writeObject("search "+username);
        out.flush();
        currentView = (Abonne) in.readObject();
        if(currentView == null){
            return false;
        }
        return true;
    }
    public static void envoyerInvitation(Invitation invitation) throws IOException {
        out.writeObject("invitation "+currentView.getUsername());
        out.flush();
        out.reset();
        out.writeObject(invitation);
        out.flush();
    }
    public static void supprimerAmi() throws IOException {
        out.writeObject("delete "+currentView.getUsername()+" "+currentUser.getUsername());
        out.flush();
    }
    public static void commenter(Commentaire commentaire, String username, int id) throws IOException {
        out.writeObject("comment "+username+" "+id);
        out.flush();
        out.reset();
        out.writeObject(commentaire);
        out.flush();
    }
    public static void partager(Publication publication, int idPublicationMere, niveauVisibilitePublication niveau) throws IOException {
        out.writeObject("share "+ClientFacebook.currentUser.getUsername()+" "+ClientFacebook.currentView.getUsername()+" "+idPublicationMere+" "+niveau.name());
        out.flush();
        out.reset();
        out.writeObject(publication);
        out.flush();
    }
    public static void reagir(TypeReaction typeReaction, int id) throws IOException {
        Reaction reaction = new Reaction(currentUser.getUsername(), typeReaction);
        out.writeObject("reaction "+currentView.getUsername()+" "+currentUser.getUsername()+" "+id);
        out.flush();
        out.reset();
        out.writeObject(reaction);
        out.flush();
    }
    public static void modifierInvitation(String etat, String username) throws IOException {
        out.writeObject("modifyInvitation "+ClientFacebook.currentUser.getUsername()+" "+username+" "+etat);
        out.flush();

    }
    //after checking the notifications, change their state to seen ( notification lue )
    public static void mettreALue() throws IOException {
        out.writeObject("seenNotifications "+ ClientFacebook.currentUser.getUsername());
        out.flush();
    }
    public static void gererEpingler(String action, String username, int id) throws IOException {
        out.writeObject(action+" "+username+" "+id);
        out.flush();
    }
}
