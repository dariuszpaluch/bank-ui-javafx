package com.dariuszpaluch.java.controllers;

import javafx.scene.control.Alert;

/**
 * Created by Dariusz Paluch on 13.01.2018.
 */
public class AlertControler {

  public void showAlert(String message, Alert.AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle("Bank alert");
    alert.setHeaderText(message);
    alert.showAndWait();
  }


  public void showErrorAlert(String message) {
    this.showAlert(message, Alert.AlertType.ERROR);
  }
}
