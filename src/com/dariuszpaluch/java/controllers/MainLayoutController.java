package com.dariuszpaluch.java.controllers;

import com.dariuszpaluch.java.BankClientService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Dariusz Paluch on 07.01.2018.
 */
@Component
public class MainLayoutController {


  public Button createBankAccountNoButton;
  public ChoiceBox selectedAccountChoiceBox;
  public Text balanceValue;
  public Button sendTransferButton;
  public TextField transferTitle;
  public TextField transferName;
  public TextField transferAmmount;
  public TextField transferDestinationAccountNoTextField;
  public Button depositMoneyButton;
  public Button withdrawMoneyButton;
  public TextField withdrawOrDepositAmount;
  private BankClientService bankClientService = BankClientService.getInstanceBankClientService();


  @FXML
  void initialize() {
    updateListOfAccounts();
//    getBalanceButton.setOnAction(this::onClickGetBalance);
    createBankAccountNoButton.setOnAction(this::onClickCreateBankAccountNo);
    selectedAccountChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        updateAccountBalance((String)selectedAccountChoiceBox.getItems().get(newValue.intValue()));
      }
    });

//    depositMoneyButton.setOnAction(this::onClickDepositMoneyButton);
//    withdrawMoneyButton.setOnAction(this::onClickWithdrawMoneyButton);
//    sendTransferButton.setOnAction(this::onClickSendTransferButton);
  }



  private void onClickSendTransferButton(ActionEvent actionEvent) {
    String destinationAccountNo = transferDestinationAccountNoTextField.getText();
    String selectedAccountNo = selectedAccountChoiceBox.getSelectionModel().getSelectedItem().toString();
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
    String selectedAccountNo = selectedAccountChoiceBox.getSelectionModel().getSelectedItem().toString();
    String withdrawAmmountString = withdrawOrDepositAmount.getText();

    if (!selectedAccountNo.isEmpty() && !withdrawAmmountString.isEmpty()) {
      int amount = Integer.parseInt(withdrawAmmountString);
      try {
        this.bankClientService.withdrawMoney(selectedAccountNo, amount);
        withdrawOrDepositAmount.setText("0");
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
  }

  private void onClickDepositMoneyButton(ActionEvent actionEvent) {
    String selectedAccountNo = selectedAccountChoiceBox.getSelectionModel().getSelectedItem().toString();

    String depositAmountString = withdrawOrDepositAmount.getText();

    if (!selectedAccountNo.isEmpty() && !depositAmountString.isEmpty()) {

      try {
        int ammount = Integer.parseInt(depositAmountString);
        this.bankClientService.depositAmount(selectedAccountNo, ammount);
        withdrawOrDepositAmount.setText("0");
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
    String selectedAccountNo = selectedAccountChoiceBox.getSelectionModel().getSelectedItem().toString();


  }

  public void updateAccountBalance(String accountNo) {
    if (!accountNo.isEmpty()) {
      int balance = 0;
      try {
        balance = this.bankClientService.getBalance(accountNo);
      } catch (Exception e) {
        e.printStackTrace();
      }
      balanceValue.setText(String.valueOf(balance));

    }
  }


  private void onClickLoginButton(ActionEvent actionEvent) {


  }

  private void updateListOfAccounts() {
    List<String> accounts = bankClientService.getUserAccounts();

    selectedAccountChoiceBox.setItems(FXCollections.observableArrayList(accounts));
    selectedAccountChoiceBox.getSelectionModel().selectFirst();
  }
}