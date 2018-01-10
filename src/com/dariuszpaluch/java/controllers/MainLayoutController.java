package com.dariuszpaluch.java.controllers;

import bank.wsdl.AuthenticateResponse;
import com.dariuszpaluch.java.BankClientService;
import com.dariuszpaluch.java.BankClientConfiguration;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Dariusz Paluch on 07.01.2018.
 */
@Component
public class MainLayoutController {
  public TextField login;
  public TextField password;
  public Button registrationButton;
  public Button loginButton;
  public Text errorText;
  public ChoiceBox selectedAccount;
  public FlowPane authorizedPane;
  public FlowPane loginPane;
  public Button getBalanceButton;
  public Text balanceValue;
  public Button depositMoneyButton;
  public TextField depositAmountTextField;
  public Button withdrawMoneyButton;
  public Text withdrawAmountTextField;

  private BankClientService bankClientService = BankClientService.getInstanceBankClientService();


  @FXML
  void initialize() {
    loginButton.setOnAction(this::onClickLoginButton);
    getBalanceButton.setOnAction(this::onClickGetBalance);
  }

  private void onClickGetBalance(ActionEvent actionEvent) {
    String selectedAccountNo = selectedAccount.getSelectionModel().getSelectedItem().toString();

    if(!selectedAccountNo.isEmpty()) {
      int balance = 0;
      try {
        balance = this.bankClientService.getBalance(selectedAccountNo);
      } catch (Exception e) {
        e.printStackTrace();
      }
      balanceValue.setText(String.valueOf(balance));

    }
  }


  private void onClickLoginButton(ActionEvent actionEvent) {
    String login = this.login.getText();
    String password = this.password.getText();

    Boolean success = bankClientService.tryAuthenticate(login, password);
    if(!success) {
      errorText.setText("Wrong login or password. Try again");
    }
    else {
      errorText.setText("SUCCESS");
      loginPane.setVisible(false);
      authorizedPane.setVisible(true);
      this.setUserAccounts();
    }
  }

  private void setUserAccounts() {
    List<String> accounts =  bankClientService.getUserAccounts();

    selectedAccount.setItems(FXCollections.observableArrayList(accounts));
  }
}
