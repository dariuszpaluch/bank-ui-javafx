package com.dariuszpaluch.java;

import bank.wsdl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.bind.JAXBElement;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPFaultElement;
import javax.xml.transform.Source;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dariusz Paluch on 07.01.2018.
 */
public class BankClientService extends WebServiceGatewaySupport {
  private static final Logger log = LoggerFactory.getLogger(BankClientService.class);
  private static BankClientService bankClientService;
  private String token;
  private static String URI = Settings.soapEndpointUrl;
  private static SoapActionCallback soapActionCallback = new SoapActionCallback(Settings.soapAction);

  public static BankClientService getInstanceBankClientService() {
    if (bankClientService == null) {
      BankClientConfiguration bankClientConfiguration = new BankClientConfiguration();
      bankClientService = bankClientConfiguration.bankClientService(bankClientConfiguration.marshaller());
    }

    return bankClientService;
  }

  private void handleServiceFault(ServiceFault serviceFault) {
    log.info("Code:" + serviceFault.getCode() + ", description " + serviceFault.getDescription());
  }

  public boolean tryAuthenticate(String login, String password) throws SoapFaultClientException {

    log.info("Requesting authenticate " + login + " " + password);

    AuthenticateRequest request = new AuthenticateRequest();
    UserAuthenticateData userAuthenticateData = new UserAuthenticateData();
    userAuthenticateData.setLogin(login);
    userAuthenticateData.setPassword(password);
    request.setUserAuthenticateData(userAuthenticateData);

    Object response = getWebServiceTemplate().marshalSendAndReceive(URI, request, soapActionCallback);
    AuthenticateResponse authenticateResponse = (AuthenticateResponse) response;
    token = authenticateResponse.getToken();
    log.info("Authenticate success");

    return true;

  }

  public List<String> getUserAccounts() {
      log.info("Try get accounts");
      GetUserAccountsRequest request = new GetUserAccountsRequest();
      MyHeaders authorizationHeader = new MyHeaders();
      authorizationHeader.setToken(token);
      Object response = getWebServiceTemplate().marshalSendAndReceive(URI, request, new SecurityHeader(authorizationHeader));

      GetUserAccountsResponse userAccountsResponse = (GetUserAccountsResponse) response;
      log.info("SUCCESS get accounts");
      return userAccountsResponse.getAccounts();
  }

  public int getBalance(String selectedAccountNo) throws Exception {
      log.info("Try get balance of " + selectedAccountNo);

      GetBalanceRequest request = new GetBalanceRequest();
      request.setAccountNo(selectedAccountNo);
      MyHeaders authorizationHeader = new MyHeaders();
      authorizationHeader.setToken(token);
      Object response = getWebServiceTemplate().marshalSendAndReceive(URI, request, new SecurityHeader(authorizationHeader));

      GetBalanceResponse getBalanceResponse = (GetBalanceResponse) response;
      log.info("get balance SUCCESS");

      return getBalanceResponse.getBalance().getBalance();
  }

  public SecurityHeader getSecurityHeader() {
    MyHeaders authorizationHeader = new MyHeaders();
    authorizationHeader.setToken(this.token);
    return new SecurityHeader(authorizationHeader);

  }

  public String createBankAccountNo() throws SoapFaultClientException {
    log.info("Try create accountNo");

    CreateAccountRequest request = new CreateAccountRequest();
    Object response = getWebServiceTemplate().marshalSendAndReceive(URI, request, this.getSecurityHeader());
    CreateAccountResponse createAccountResponse = (CreateAccountResponse) response;
    return createAccountResponse.getAccountNo();
  }

  public void depositAmount(String selectedAccountNo, int ammount) throws SoapFaultClientException {
    DepositMoneyRequest request = new DepositMoneyRequest();
    request.setAccountNo(selectedAccountNo);
    request.setAmount(ammount);
    Object response = getWebServiceTemplate().marshalSendAndReceive(URI, request, this.getSecurityHeader());
    DepositMoneyResponse depositMoneyResponse = (DepositMoneyResponse) response;
  }

  public void withdrawMoney(String selectedAccountNo, int ammount) throws SoapFaultClientException {
    log.info("Try withdraw Amount");
    WithdrawMoneyRequest request = new WithdrawMoneyRequest();
    request.setAccountNo(selectedAccountNo);
    request.setAmount(ammount);
    Object response = getWebServiceTemplate().marshalSendAndReceive(URI, request, this.getSecurityHeader());
    WithdrawMoneyResponse withdrawMoneyResponse = (WithdrawMoneyResponse) response;
  }

  public void sendTransfer(String sourceAccountNo, String destinationAccountNo, String title, String name, int ammount) throws SoapFaultClientException {
      log.info("Try Transfer");
      TransferRequest request = new TransferRequest();
      Transfer transfer = new Transfer();
      transfer.setSourceAccount(sourceAccountNo);
      transfer.setDestinationAccount(destinationAccountNo);
      transfer.setTitle(title);
      transfer.setName(name);
      transfer.setAmount(ammount);
      request.setTransfer(transfer);


      Object response = getWebServiceTemplate().marshalSendAndReceive(URI, request, this.getSecurityHeader());
      TransferResponse transferResponse = (TransferResponse) response;
  }

  public List<OperationHistory> getAccountOperationHistory(String selectedAccountNo) throws SoapFaultClientException {
    GetAccountHistoryRequest request = new GetAccountHistoryRequest();
    request.setAccountNo(selectedAccountNo);
    MyHeaders authorizationHeader = new MyHeaders();
    authorizationHeader.setToken(token);
    Object response = getWebServiceTemplate().marshalSendAndReceive(URI, request, new SecurityHeader(authorizationHeader));

    GetAccountHistoryResponse result = (GetAccountHistoryResponse) response;
    return result.getOperations();
  }
}
