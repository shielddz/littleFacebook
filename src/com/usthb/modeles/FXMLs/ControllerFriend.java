package com.usthb.modeles.FXMLs;

import com.usthb.modeles.ClientFacebook;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerFriend implements Initializable {
    @FXML
    Label name;
    @FXML
    ImageView connected;
    @FXML
    ImageView disconnected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setText(ClientFacebook.abonneTmp.nom+" "+ClientFacebook.abonneTmp.prenom);
        if(ClientFacebook.abonneTmp.online){
            connected.setVisible(true);
            return;
        }
        disconnected.setVisible(true);
    }
}
