package com.usthb.modeles.FXMLs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.usthb.modeles.ClientFacebook;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ControllerPublication implements Initializable{
    private boolean isPinned = false;
    public String username;
    public int id;
    ObservableList<String> viewLevelList = FXCollections.observableArrayList("public", "amis", "prive");
    @FXML
    JFXComboBox viewLevelBox;
    @FXML
    private Label nLike;
    @FXML
    private Label nLove;
    @FXML
    private Label nGai;
    @FXML
    private Label nSad;
    @FXML
    private Label nAngry;
    @FXML
    public Label content;
    @FXML
    public JFXListView comments;
    @FXML
    public Label publisher;
    @FXML
    JFXButton submitComment;
    @FXML
    JFXTextField commentContent;
    @FXML
    Label shares;
    @FXML
    Label level;
    @FXML
    JFXButton pin;
    @FXML
    Label pinned;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(ClientFacebook.publicationTmp.isEpinglee()){
            pinned.setText("[Epinglée]");
            pin.setText("Desepingler");
            isPinned = true;
        }
        pin.setVisible(false);
        if(ClientFacebook.currentUser.getUsername().equals(ClientFacebook.currentView.getUsername())){
            pin.setVisible(true);
        }
        viewLevelBox.setValue("public");
        viewLevelBox.setItems(viewLevelList);
        String nameToDisplay=ClientFacebook.currentView.nom+" "+ClientFacebook.currentView.prenom;
        content.setText(ClientFacebook.publicationTmp.getContenu());
        level.setText("["+convert(ClientFacebook.publicationTmp.getNiveau().name())+"]");
        //set the numbers in the labels ( the reactions Lables)
        nLike.setText(String.valueOf(countReaction(TypeReaction.jAime)));
        nLove.setText(String.valueOf(countReaction(TypeReaction.jAdore)));
        nGai.setText(String.valueOf(countReaction(TypeReaction.gai)));
        nSad.setText(String.valueOf(countReaction(TypeReaction.triste)));
        nAngry.setText(String.valueOf(countReaction(TypeReaction.enColere)));

        shares.setText(ClientFacebook.publicationTmp.nombrePartages+" Partages.");
        if(ClientFacebook.publicationTmp.abonnePere != null){
            nameToDisplay += " ( partagée depuis le profile de "+ClientFacebook.publicationTmp.abonnePere.nom+" "+ClientFacebook.publicationTmp.abonnePere.prenom+" ).";
        }else{
            nameToDisplay += ".";
        }
        publisher.setText(nameToDisplay);
        comments.setPrefHeight(45*ClientFacebook.publicationTmp.getCommentaires().size());
        id = ClientFacebook.publicationTmp.numero;
        username = ClientFacebook.currentView.getUsername();
        for (Commentaire commentaire:ClientFacebook.publicationTmp.getCommentaires()) {
            ClientFacebook.commentTmp = commentaire;
            Parent commentView = null;
            try {
                commentView = FXMLLoader.load(getClass().getResource("commentaire.fxml"));
                comments.getItems().add(commentView);
            } catch (IOException e) {
            }
            ClientFacebook.commentTmp = null;
        }
    }
    @FXML
    private void submitComment(ActionEvent event) throws IOException, ClassNotFoundException {
        if(commentContent.getText().isEmpty()){
            return;
        }
        Commentaire commentaire = new Commentaire(commentContent.getText(), ClientFacebook.currentUser);
        ClientFacebook.commenter(commentaire, username, id);
        reload(event);
    }
    @FXML
    private void share(ActionEvent event) throws IOException, ClassNotFoundException {
        Publication publication = new Publication(content.getText(), niveauVisibilitePublication.publiq, false);
        niveauVisibilitePublication nvp = niveauVisibilitePublication.valueOf(reverseConvert("public"));
        if(viewLevelBox.getValue() != null){
            nvp = niveauVisibilitePublication.valueOf(reverseConvert((String)viewLevelBox.getValue()));
        }
        ClientFacebook.partager(publication, id, nvp);
        reload(event);
    }
    @FXML
    void pin(ActionEvent event) throws IOException, ClassNotFoundException {
        ClientFacebook.gererEpingler((isPinned ? "unpin" : "pin"), username, id);
        reload(event);
    }
    @FXML
    void angry(ActionEvent event) throws IOException, ClassNotFoundException {
        ClientFacebook.reagir(TypeReaction.enColere, id);
        reload(event);
        //remove the old reaction ( in the server )
    }
    @FXML
    void gai(ActionEvent event) throws IOException, ClassNotFoundException {
        ClientFacebook.reagir(TypeReaction.gai, id);
        reload(event);
    }
    @FXML
    void like(ActionEvent event) throws IOException, ClassNotFoundException {
        ClientFacebook.reagir(TypeReaction.jAime, id);
        reload(event);
    }
    @FXML
    void love(ActionEvent event) throws IOException, ClassNotFoundException {
        ClientFacebook.reagir(TypeReaction.jAdore, id);
        reload(event);
    }
    @FXML
    void sad(ActionEvent event) throws IOException, ClassNotFoundException {
        ClientFacebook.reagir(TypeReaction.triste, id);
        reload(event);
    }

    private void reload(ActionEvent event) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("index.fxml"));
        Parent index = loader.load();
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(new Scene(index));
        window.show();
        ControllerIndex controllerIndex = loader.getController();
        controllerIndex.setName();
        controllerIndex.setNameProfile();
        controllerIndex.setAboutPane();
        controllerIndex.showWallPane();
        controllerIndex.setInvitationsPane();
        controllerIndex.setNotificationsPane();
    }
    private int countReaction(TypeReaction typeReaction){
        int ret = 0;
        for (Reaction reaction:ClientFacebook.publicationTmp.reactions) {
            if(reaction.getReaction() == typeReaction){
                ret++;
            }
        }
        return ret;
    }
    private String convert(String convert){
        if(convert.equals("publiq")){
            return "public";
        }
        return convert;
    }
    private String reverseConvert(String niveau){
        if(niveau.equals("public")){
            return "publiq";
        }
        return niveau;
    }
}
