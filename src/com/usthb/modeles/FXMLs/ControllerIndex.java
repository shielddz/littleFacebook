package com.usthb.modeles.FXMLs;

import com.jfoenix.controls.*;
import com.usthb.modeles.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;

public class ControllerIndex {
    ObservableList<String> viewLevelList = FXCollections.observableArrayList("public", "amis", "prive");
    static boolean activeInvitations = false;
    static boolean activeNotifications = false;
    @FXML
    JFXButton name;
    @FXML
    JFXTextField search;
    @FXML
    Label nameProfile;
    @FXML
    Label aboutSpecialite;
    @FXML
    Label aboutNiveau;
    @FXML
    Label aboutFonction;
    @FXML
    Label aboutDateDeNaissance;
    @FXML
    Label aboutSexe;
    @FXML
    JFXButton addFriend;
    @FXML
    JFXButton validateAdd;
    @FXML
    JFXButton deleteFriend;
    @FXML
    Pane aboutPane;
    @FXML
    Pane wallPane;
    @FXML
    Pane invitationsPane;
    @FXML
    Pane notificationsPane;
    @FXML
    JFXListView publications;
    @FXML
    Pane friendsPane;
    @FXML
    JFXListView friendsList;
    @FXML
    JFXTextArea publicationContent;
    @FXML
    HBox publicationBox;
    @FXML
    HBox confirmAdd;
    @FXML
    Pane mainProfilePane;
    @FXML
    private
    Pane errorMessagePane;
    @FXML
    JFXTextField requestMessage;
    @FXML
    JFXComboBox viewLevelBox;
    @FXML
    JFXListView invitations;
    @FXML
    JFXListView notifications;
    @FXML
    Label numberInvitations;
    @FXML
    Label numberNotifications;


    @FXML
    private void disconnect(ActionEvent event) throws IOException {
        Parent login = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(new Scene(login));
        window.show();
        ClientFacebook.disconnect();
    }
    @FXML
    private void showInvitations() throws IOException, ClassNotFoundException {
        if(activeInvitations) {
            invitationsPane.setVisible(false);
            activeInvitations = false;
            return;
        }
        if(activeNotifications){
            notificationsPane.setVisible(false);
            activeNotifications = false;
            seeNotifications();
        }
        invitationsPane.setVisible(true);
        activeInvitations = true;
    }
    @FXML
    private void showNotifications() throws IOException, ClassNotFoundException {
        if(activeNotifications) {
            notificationsPane.setVisible(false);
            activeNotifications = false;
            seeNotifications();
            return;
        }
        invitationsPane.setVisible(false);
        activeInvitations = false;
        notificationsPane.setVisible(true);
        activeNotifications = true;
    }
    @FXML
    private void showProfile() throws IOException, ClassNotFoundException {
        if(ClientFacebook.currentView != null){
            if(ClientFacebook.currentView.getUsername().equals(ClientFacebook.currentUser.getUsername())) {
                return;
            }
        }
        errorMessagePane.setVisible(false);
        mainProfilePane.setVisible(true);
        ClientFacebook.currentView = ClientFacebook.currentUser;
        setNameProfile();
        setAboutPane();
        showWallPane();
    }
    @FXML
    private void search() throws IOException, ClassNotFoundException {
        notificationsPane.setVisible(false);
        activeNotifications = false;
        invitationsPane.setVisible(false);
        activeInvitations = false;
        boolean check;
        check = ClientFacebook.rechercher(search.getText());
        if(!check){
            errorMessagePane.setVisible(true);
            mainProfilePane.setVisible(false);
            friendsPane.setVisible(false);
            aboutPane.setVisible(false);
            wallPane.setVisible(false);
            return;
        }
        errorMessagePane.setVisible(false);
        setNameProfile();
        setAboutPane();
        showWallPane();
    }
    @FXML
    private void showAboutPane(){
        wallPane.setVisible(false);
        friendsPane.setVisible(false);
        aboutPane.setVisible(true);
    }
    @FXML
    public void showWallPane() throws IOException, ClassNotFoundException {
        //hide notifications and invitations
        notificationsPane.setVisible(false);
        activeNotifications = false;
        invitationsPane.setVisible(false);
        activeInvitations = false;
        //panes
        wallPane.setVisible(true);
        friendsPane.setVisible(false);
        aboutPane.setVisible(false);
        //sections and buttons
        addFriend.setVisible(false);
        deleteFriend.setVisible(false);
        confirmAdd.setVisible(false);
        ClientFacebook.update();
        viewLevelBox.setValue("public");
        viewLevelBox.setItems(viewLevelList);
        if(!ClientFacebook.currentUser.getUsername().equals(ClientFacebook.currentView.getUsername())){
            if(!ClientFacebook.currentUser.getAmis().contains(ClientFacebook.currentView.getUsername())){
                if(!ClientFacebook.currentView.aInvitation(ClientFacebook.currentUser.getUsername())
                && !ClientFacebook.currentUser.aInvitation(ClientFacebook.currentView.getUsername())){
                    if(ClientFacebook.currentUser.aAmi(ClientFacebook.currentView.getUsername())){
                        deleteFriend.setVisible(true);
                    }else{
                        addFriend.setVisible(true);
                    }
                }
            }
            publicationBox.setDisable(true);
        }else{
            publicationBox.setDisable(false);
        }
        publications.getItems().clear();
        Collections.reverse(ClientFacebook.currentView.getPublications());
        //show pinned first
        for (Publication pub:ClientFacebook.currentView.getPublications()) {
            ClientFacebook.publicationTmp = pub;
            if(ClientFacebook.publicationTmp.isEpinglee()) {
                if (ClientFacebook.currentView.getUsername().equals(ClientFacebook.currentUser.getUsername())
                        || ClientFacebook.publicationTmp.getNiveau() == niveauVisibilitePublication.publiq) {
                    Parent publicationView = FXMLLoader.load(getClass().getResource("publication.fxml"));
                    publications.getItems().add(publicationView);
                    ClientFacebook.publicationTmp = null;
                } else {
                    if (ClientFacebook.publicationTmp.getNiveau() == niveauVisibilitePublication.amis) {
                        if (ClientFacebook.currentView.aAmi(ClientFacebook.currentUser.getUsername())) {
                            Parent publicationView = FXMLLoader.load(getClass().getResource("publication.fxml"));
                            publications.getItems().add(publicationView);
                            ClientFacebook.publicationTmp = null;
                        }
                    }
                }
            }
            ClientFacebook.publicationTmp = null;
        }
        //show unpinned
        for (Publication pub:ClientFacebook.currentView.getPublications()) {
            ClientFacebook.publicationTmp = pub;
            if(!ClientFacebook.publicationTmp.isEpinglee()) {
                if (ClientFacebook.currentView.getUsername().equals(ClientFacebook.currentUser.getUsername())
                        || ClientFacebook.publicationTmp.getNiveau() == niveauVisibilitePublication.publiq) {
                    Parent publicationView = FXMLLoader.load(getClass().getResource("publication.fxml"));
                    publications.getItems().add(publicationView);
                    ClientFacebook.publicationTmp = null;
                } else {
                    if (ClientFacebook.publicationTmp.getNiveau() == niveauVisibilitePublication.amis) {
                        if (ClientFacebook.currentView.aAmi(ClientFacebook.currentUser.getUsername())) {
                            Parent publicationView = FXMLLoader.load(getClass().getResource("publication.fxml"));
                            publications.getItems().add(publicationView);
                            ClientFacebook.publicationTmp = null;
                        }
                    }
                }
            }
            ClientFacebook.publicationTmp = null;
        }
    }
    @FXML
    private void showFriendsPane() throws IOException, ClassNotFoundException {
        wallPane.setVisible(false);
        friendsPane.setVisible(true);
        aboutPane.setVisible(false);
        ClientFacebook.update();
        friendsList.getItems().clear();
        for (Abonne abonne:ClientFacebook.currentView.getAmis()) {
            ClientFacebook.abonneTmp = abonne;
            Parent friendView = FXMLLoader.load(getClass().getResource("friend.fxml"));
            friendsList.getItems().add(friendView);
            ClientFacebook.abonneTmp = null;
        }
    }
    @FXML
    private void publish() throws IOException, ClassNotFoundException {
        if(publicationContent.getText().isEmpty()){
            return;
        }
        niveauVisibilitePublication nvp = niveauVisibilitePublication.valueOf(convert("public"));
        if(viewLevelBox.getValue() != null){
            nvp = niveauVisibilitePublication.valueOf(convert((String)viewLevelBox.getValue()));
        }
        Publication publication = new Publication(publicationContent.getText(), nvp);
        ClientFacebook.publier(publication);
        showWallPane();
        publicationContent.clear();
    }
    @FXML
    private void addFriend(){
        confirmAdd.setVisible(true);
    }
    @FXML
    private void validateAdd() throws IOException, ClassNotFoundException {
        Invitation invitation = new Invitation(ClientFacebook.currentUser.getUsername(), requestMessage.getText());
        ClientFacebook.envoyerInvitation(invitation);
        showWallPane();
    }
    @FXML
    private void deleteFriend() throws IOException, ClassNotFoundException {
        ClientFacebook.supprimerAmi();
        showWallPane();
    }


    public void setName(){
        name.setText(ClientFacebook.currentUser.nom+" "+ClientFacebook.currentUser.prenom);
    }
    public void setNameProfile(){
        mainProfilePane.setVisible(true);
        nameProfile.setText(ClientFacebook.currentView.nom+" "+ClientFacebook.currentView.prenom); }
    //user in setAboutPane
    public static int getAge(Abonne abonne){
        int ret;
        ret = Calendar.getInstance().get(Calendar.YEAR) - abonne.getDateDeNaissance().année;
        if(Calendar.getInstance().get(Calendar.MONTH) < abonne.getDateDeNaissance().mois && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < abonne.getDateDeNaissance().jour ){
            ret--;
        }
        return ret;
    }
    public void setAboutPane(){
        aboutSpecialite.setText("Specialité : "+ClientFacebook.currentView.getSpecialite());
        aboutNiveau.setText("Niveau : "+ClientFacebook.currentView.getNiveau());
        aboutFonction.setText("Fonction : "+ClientFacebook.currentView.getFonction());
        aboutDateDeNaissance.setText("Date de naissance - Age : "+ClientFacebook.currentView.getDateDeNaissance()+" - "+ getAge(ClientFacebook.currentView));
        aboutSexe.setText("Sexe : "+ClientFacebook.currentView.getCategorie().name());
    }
    public void setInvitationsPane() throws IOException {
        invitations.getItems().clear();
        ClientFacebook.numberInvitations=0;
        Collections.reverse(ClientFacebook.currentUser.getInvitations());
        for(Invitation invitation:ClientFacebook.currentUser.getInvitations()){
            ClientFacebook.invitationTmp = invitation;
            Parent invitationView = FXMLLoader.load(getClass().getResource("invitation.fxml"));
            invitations.getItems().add(invitationView);
            ClientFacebook.invitationTmp = null;
        }
        numberInvitations.setText(" "+ClientFacebook.numberInvitations+" ");
    }
    public void setNotificationsPane() throws IOException {
        notifications.getItems().clear();
        ClientFacebook.numberNotifications=0;
        Collections.reverse(ClientFacebook.currentUser.getNotifications());
        for(Notification notification:ClientFacebook.currentUser.getNotifications()){
            ClientFacebook.notificationTmp = notification;
            Parent notificationview = FXMLLoader.load(getClass().getResource("notification.fxml"));
            notifications.getItems().add(notificationview);
            ClientFacebook.notificationTmp = null;
        }
        numberNotifications.setText(" "+ClientFacebook.numberNotifications+" ");
    }
    public void seeNotifications() throws IOException, ClassNotFoundException {
        ClientFacebook.mettreALue();
        ClientFacebook.update();
        setNotificationsPane();
    }
    private String convert(String niveau){
        if(niveau.equals("public")){
            return "publiq";
        }
        return niveau;
    }
}
