package com.usthb.modeles.FXMLs;

import com.usthb.modeles.ClientFacebook;
import com.usthb.modeles.TypeNotification;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerNotification implements Initializable {
    @FXML
    Label notificationContent;
    @FXML
    Label status;
    @FXML
    ImageView reaction;
    @FXML
    ImageView comment;
    @FXML
    ImageView post;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notificationContent.setText(String.valueOf(ClientFacebook.notificationTmp.getInformation()));
        if (ClientFacebook.notificationTmp.isLue()) {
            status.setText("Lue.");
        }else {
            status.setText("Non lue.");
            ClientFacebook.numberNotifications++;
        }
        if(ClientFacebook.notificationTmp.getType() == TypeNotification.publication){
            post.setVisible(true);
        }else if(ClientFacebook.notificationTmp.getType() == TypeNotification.commentaire){
            comment.setVisible(true);
        }else{
            reaction.setVisible(true);
        }
    }
}
