package com.dariuszpaluch.java.controllers;

import com.dariuszpaluch.java.BankClientService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.client.SoapFaultClientException;

import java.util.List;

/**
 * Created by Dariusz Paluch on 07.01.2018.
 */
@Component
public class MainLayoutController {
  public Text errorText;
  public ChoiceBox selectedAccount;
  public BorderPane authorizedPane;
  public FlowPane loginPane;
  public Button getBalanceButton;
  public Text balanceValue;
  public Button depositMoneyButton;
  public TextField depositAmountTextField;
  public Button withdrawMoneyButton;
  public TextField withdrawAmountTextField;
  public Button createBankAccountNoButton;
  public Button sendTransferButton;
  public TextField transferDestinationAccountNoTextField;
  public TextField transferName;
  public TextField transferTitle;
  public TextField transferAmmount;

  private BankClientService bankClientService = BankClientService.getInstanceBankClientService();


  @FXML
  void initialize() {
//    getBalanceButton.setOnAction(this::onClickGetBalance);
//    createBankAccountNoButton.setOnAction(this::onClickCreateBankAccountNo);
//    depositMoneyButton.setOnAction(this::onClickDepositMoneyButton);
//    withdrawMoneyButton.setOnAction(this::onClickWithdrawMoneyButton);
//    sendTransferButton.setOnAction(this::onClickSendTransferButton);
  }



  private void onClickSendTransferButton(ActionEvent actionEvent) {
    String destinationAccountNo = transferDestinationAccountNoTextField.getText();
    String selectedAccountNo = selectedAccount.getSelectionModel().getSelectedItem().toString();
    String title = transferTitle.getText();
    String name = transferName.getText();
    String ammount = transferAmmount.getText();

    if (!destinationAccountNo.isEmpty() && !title.isEmpty() && !name.isEmpty() && !ammount.isEmpty() && !selectedAccountNo.isEmpty()){
      try {
        this.bankClientService.sendTransfer(selectedAccountNo, destinationAccountNo, title, name, Integer.parseInt(ammount));
      } catch (Exception e) {
//        showErrorAlert("Wrong transfer");
      }
    } else{
//      showErrorAlert("Invalid data to transfer");
    }
  }

  private void onClickWithdrawMoneyButton(ActionEvent actionEvent) {
    String selectedAccountNo = selectedAccount.getSelectionModel().getSelectedItem().toString();
    String withdrawAmmountString = withdrawAmountTextField.getText();

    if (!selectedAccountNo.isEmpty() && !withdrawAmmountString.isEmpty()) {
      int amount = Integer.parseInt(withdrawAmmountString);
      try {
        this.bankClientService.withdrawMoney(selectedAccountNo, amount);
        withdrawAmountTextField.setText("0");
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
  }

  private void onClickDepositMoneyButton(ActionEvent actionEvent) {
    String selectedAccountNo = selectedAccount.getSelectionModel().getSelectedItem().toString();

    String depositAmountString = depositAmountTextField.getText();

    if (!selectedAccountNo.isEmpty() && !depositAmountString.isEmpty()) {

      try {
        int ammount = Integer.parseInt(depositAmountString);
        this.bankClientService.depositAmount(selectedAccountNo, ammount);
        depositAmountTextField.setText("0");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void onClickCreateBankAccountNo(ActionEvent actionEvent) {
    try {
      String accountNo = this.bankClientService.createBankAccountNo();
      this.updateListOfAccounts();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void onClickGetBalance(ActionEvent actionEvent) {
    String selectedAccountNo = selectedAccount.getSelectionModel().getSelectedItem().toString();

    if (!selectedAccountNo.isEmpty()) {
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


  }

  private void afterSuccessLogin() {
    errorText.setText("SUCCESS");
    loginPane.setVisible(false);
    authorizedPane.setVisible(true);
    this.setUserAccounts();
  }

  private void setUserAccounts() {
    this.updateListOfAccounts();
  }

  private void updateListOfAccounts() {
    List<String> accounts = bankClientService.getUserAccounts();

    selectedAccount.setItems(FXCollections.observableArrayList(accounts));
  }
}