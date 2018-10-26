package com.usthb.modeles.FXMLs;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.usthb.modeles.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class ControllerLogIn {

    //fields
    @FXML
    Label name;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private Label errorMessage;

    //functions
    @FXML
    private void ChangeToSignUp(ActionEvent event) throws IOException {
        Parent signup = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(new Scene(signup));
        window.show();
    }
    @FXML
    private void Connect(ActionEvent event) throws IOException, ClassNotFoundException {
        String myUsername = username.getText();
        String myPassword = password.getText();
        boolean check;
        if(myUsername.isEmpty() || myPassword.isEmpty()){
            check = false;
        }else {
            check = ClientFacebook.connect(myUsername, myPassword);
        }

        if (check) {
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
            errorMessage.setVisible(false);
        } else {
            errorMessage.setVisible(true);
        }
    }
}
