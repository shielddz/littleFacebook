package com.usthb.modeles.FXMLs;

import com.usthb.modeles.ClientFacebook;
import com.usthb.modeles.EtatInvitation;
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
import java.util.ResourceBundle;

public class ControllerInvitation implements Initializable {
    String username;
    @FXML
    Label name;
    @FXML
    Label accepted;
    @FXML
    Label declined;
    @FXML
    Pane status;
    @FXML
    Label message;
    @FXML
    HBox action;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username = ClientFacebook.invitationTmp.getUser();
        name.setText(ClientFacebook.invitationTmp.nom+" "+ClientFacebook.invitationTmp.prenom);
        message.setText(ClientFacebook.invitationTmp.getMessage());
        if(ClientFacebook.invitationTmp.getEtat() == EtatInvitation.instance){
            status.setVisible(false);
            action.setVisible(true);
            ClientFacebook.numberInvitations++;
        }else if(ClientFacebook.invitationTmp.getEtat() == EtatInvitation.refusee){
            status.setVisible(true);
            accepted.setVisible(false);
            declined.setVisible(true);
        }else{
            status.setVisible(true);
            accepted.setVisible(true);
            declined.setVisible(false);
        }

    }
    @FXML
    private void accept(ActionEvent event) throws IOException, ClassNotFoundException {
        ClientFacebook.modifierInvitation("acceptee", username);
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
    @FXML
    private void decline(ActionEvent event) throws IOException, ClassNotFoundException {
        ClientFacebook.modifierInvitation("refusee", username);
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
}
