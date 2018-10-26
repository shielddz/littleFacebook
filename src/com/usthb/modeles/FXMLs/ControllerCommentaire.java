package com.usthb.modeles.FXMLs;

import com.usthb.modeles.ClientFacebook;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerCommentaire implements Initializable {
    @FXML
    Label user;
    @FXML
    Label commentContent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user.setText(ClientFacebook.commentTmp.user.nom+" "+ClientFacebook.commentTmp.user.prenom);
        commentContent.setText(ClientFacebook.commentTmp.getCommentaire());
    }
}
