package com.dariuszpaluch.java.controllers;

import com.dariuszpaluch.java.BankClientService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.client.SoapFaultClientException;

import java.io.IOException;

/**
 * Created by Dariusz Paluch on 13.01.2018.
 */

@Component
public class LoginController {
  public Button loginButton;
  public TextField login;
  public PasswordField password;
  private BankClientService bankClientService = BankClientService.getInstanceBankClientService();
  private AlertControler alertControler = new AlertControler();
  @FXML
  void initialize() {
    loginButton.setOnAction(this::onClickLoginButton);
  }

  private void onClickLoginButton(ActionEvent actionEvent) {

    String login = this.login.getText();
    String password = this.password.getText();

    try {
      Boolean success = bankClientService.tryAuthenticate(login, password);

      if (!success) {
        alertControler.showErrorAlert("Wrong login or password. Try again");
      } else {
        this.goToAuthorizedView(actionEvent);
      }

    } catch (SoapFaultClientException soapFaultClientException) {
      alertControler.showErrorAlert(soapFaultClientException.getFaultStringOrReason());
    }




  }

  private void goToAuthorizedView(ActionEvent actionEvent) {
    try {
      Parent authorizedVieParent = new FXMLLoader(getClass()  .getResource("/fxml/MainLayout.fxml")).load();
      Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
      stageTheEventSourceNodeBelongs.setScene(new Scene(authorizedVieParent));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }



}
