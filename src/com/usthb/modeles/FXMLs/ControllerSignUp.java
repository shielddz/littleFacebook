package com.usthb.modeles.FXMLs;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.usthb.modeles.Abonne;
import com.usthb.modeles.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerSignUp {

    //fields
    @FXML
    Label error;
    @FXML
    JFXTextField nom;
    @FXML
    JFXTextField prenom;
    @FXML
    JFXTextField username;
    @FXML
    JFXTextField specialite;
    @FXML
    JFXTextField fonction;
    @FXML
    JFXTextField niveau;
    @FXML
    JFXPasswordField password;
    @FXML
    JFXDatePicker date;
    @FXML
    JFXRadioButton homme;
    @FXML
    JFXRadioButton femme;

    //functions
    @FXML
    private void ChangeToLogIn(ActionEvent event) throws IOException {
        Parent login = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(new Scene(login));
        window.show();
    }

    @FXML
    private void SignUp(ActionEvent event) throws IOException {
        boolean check;
        if(nom.getText().isEmpty() || prenom.getText().isEmpty() || username.getText().isEmpty() || specialite.getText().isEmpty() ||
                fonction.getText().isEmpty() || password.getText().isEmpty() || niveau.getText().isEmpty() ||
                (!homme.isSelected() && !femme.isSelected()) || (date.getValue() == null)){
            check = false;
        }else {


            MaDate dateDeNaissance = new MaDate(date.getValue().getDayOfMonth(), date.getValue().getMonthValue(), date.getValue().getYear());
            Categorie categorie = ((homme.isSelected()) ? Categorie.homme : Categorie.femme);
            Abonne abonne = new Abonne(nom.getText(), prenom.getText(), username.getText(), password.getText(), specialite.getText(), fonction.getText(), niveau.getText(), dateDeNaissance, categorie);
            check = ClientFacebook.signUp(abonne);
        }
        if(check){
            error.setVisible(false);
            Parent signup = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(new Scene(signup));
            window.show();
        }else{
            error.setVisible(true);
            Abonne.supprimerAbonne();
        }
    }
}
