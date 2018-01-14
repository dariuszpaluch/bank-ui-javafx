package com.dariuszpaluch.java.controllers;

import bank.wsdl.OperationHistory;
import com.dariuszpaluch.java.BankClientService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.client.SoapFaultClientException;

import java.util.Collections;
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
  public TableView<OperationHistory> table;
  public TableColumn amountColumn;
  public TableColumn typeColumn;
  public TableColumn titleColumn;
  public TableColumn nameColumn;
  public TableColumn balanceColumn;
  public TableColumn destinationColumn;
  public TableColumn sourceColumn;
  public TableColumn dateColumn;
  public MenuItem refreshMenuItem;

  private BankClientService bankClientService = BankClientService.getInstanceBankClientService();

  private AlertControler alertControler = new AlertControler();

  @FXML
  void initialize() {
    updateListOfAccounts();
//    getBalanceButton.setOnAction(this::onClickGetBalance);
    createBankAccountNoButton.setOnAction(this::onClickCreateBankAccountNo);
    selectedAccountChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
      if((int)newValue >= 0) {
        updateAccountBalance((String) selectedAccountChoiceBox.getItems().get(newValue.intValue()));
      }
    });


    depositMoneyButton.setOnAction(this::onClickDepositMoneyButton);
    withdrawMoneyButton.setOnAction(this::onClickWithdrawMoneyButton);
    sendTransferButton.setOnAction(this::onClickSendTransferButton);
    refreshMenuItem.setOnAction(this::onClickRefreshButton);

    amountColumn.setCellValueFactory(new PropertyValueFactory<OperationHistory, Integer>("amount"));
    typeColumn.setCellValueFactory(new PropertyValueFactory<OperationHistory, String>("operationType"));
    titleColumn.setCellValueFactory(new PropertyValueFactory<OperationHistory, String>("title"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<OperationHistory, String>("name"));
    balanceColumn.setCellValueFactory(new PropertyValueFactory<OperationHistory, Integer>("balance"));
    destinationColumn.setCellValueFactory(new PropertyValueFactory<OperationHistory, String>("destinationAccount"));
    sourceColumn.setCellValueFactory(new PropertyValueFactory<OperationHistory, String>("sourceAccount"));
    dateColumn.setCellValueFactory(new PropertyValueFactory<OperationHistory, String>("operationDate"));
  }

  private void onClickRefreshButton(ActionEvent actionEvent) {
    this.updateListOfAccounts();
  }


  private void onClickSendTransferButton(ActionEvent actionEvent) {
    String destinationAccountNo = transferDestinationAccountNoTextField.getText();
    String selectedAccountNo = selectedAccountChoiceBox.getSelectionModel().getSelectedItem().toString();
    String title = transferTitle.getText();
    String name = transferName.getText();
    String ammount = transferAmmount.getText();

    if (!destinationAccountNo.isEmpty() && !title.isEmpty() && !name.isEmpty() && !ammount.isEmpty() && !selectedAccountNo.isEmpty()) {
      try {
        this.bankClientService.sendTransfer(selectedAccountNo, destinationAccountNo, title, name, Integer.parseInt(ammount));
        this.updateAccountBalance(selectedAccountNo);
        transferAmmount.setText("0");
        transferName.clear();
        transferTitle.clear();
        transferDestinationAccountNoTextField.clear();
      } catch (Exception e) {
//        showErrorAlert("Wrong transfer");
      }
    } else {
//      showErrorAlert("Invalid data to transfer");
    }
  }

  private void onClickWithdrawMoneyButton(ActionEvent actionEvent) {
    String selectedAccountNo = selectedAccountChoiceBox.getSelectionModel().getSelectedItem().toString();
    String withdrawAmmountString = withdrawOrDepositAmount.getText();

    try {

      int amount = Integer.parseInt(withdrawAmmountString);
      if (!withdrawAmmountString.isEmpty() && Integer.parseInt(withdrawAmmountString) > 0) {
        try {
          this.bankClientService.withdrawMoney(selectedAccountNo, amount);
          withdrawOrDepositAmount.setText("0");
          this.updateAccountBalance(selectedAccountNo);
        } catch (SoapFaultClientException soapFaultClientException) {
          this.alertControler.showErrorAlert(soapFaultClientException.getFaultStringOrReason());
        }
      } else {
        this.alertControler.showErrorAlert("Wrong ammount value");
      }
    } catch (NumberFormatException e) {
      this.alertControler.showErrorAlert("Wrong ammount value");
    }
  }

  private void onClickDepositMoneyButton(ActionEvent actionEvent) {
    String selectedAccountNo = selectedAccountChoiceBox.getSelectionModel().getSelectedItem().toString();

    String depositAmountString = withdrawOrDepositAmount.getText();
    int amount = Integer.parseInt(depositAmountString);

    try {
      if (!selectedAccountNo.isEmpty() && amount > 0) {
        try {

          this.bankClientService.depositAmount(selectedAccountNo, amount);
          withdrawOrDepositAmount.setText("0");
          this.updateAccountBalance(selectedAccountNo);
        } catch (SoapFaultClientException soapFaultClientException) {
          this.alertControler.showErrorAlert(soapFaultClientException.getFaultStringOrReason());
        }
      }
    } catch (NumberFormatException e) {
      this.alertControler.showErrorAlert("Wrong ammount value");
    }
  }

  private void onClickCreateBankAccountNo(ActionEvent actionEvent) {
    try {
      String accountNo = this.bankClientService.createBankAccountNo();
      this.updateListOfAccounts();
    } catch (SoapFaultClientException soapFaultClientException) {
      this.alertControler.showErrorAlert(soapFaultClientException.getFaultStringOrReason());
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
    updateListOfAccountHistoryOperations(accountNo);
  }

  private void updateListOfAccounts() {
    List<String> accounts = bankClientService.getUserAccounts();

    selectedAccountChoiceBox.setItems(FXCollections.observableArrayList(accounts));
    selectedAccountChoiceBox.getSelectionModel().selectLast();
    updateAccountBalance(selectedAccountChoiceBox.getSelectionModel().getSelectedItem().toString());
//    updateListOfAccountHistoryOperations();
  }

  public void updateListOfAccountHistoryOperations(String selectedAccountNo) {

      if (!selectedAccountNo.isEmpty()) {
        List<OperationHistory> history = this.bankClientService.getAccountOperationHistory(selectedAccountNo);
        Collections.reverse(history);
        table.setItems(FXCollections.observableArrayList(history));
      }
  }
}