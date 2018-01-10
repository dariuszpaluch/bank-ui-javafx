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

  public boolean tryAuthenticate(String login, String password) {

    log.info("Requesting authenticate " + login + " " + password);

    AuthenticateRequest request = new AuthenticateRequest();
    UserAuthenticateData userAuthenticateData = new UserAuthenticateData();
    userAuthenticateData.setLogin(login);
    userAuthenticateData.setPassword(password);
    request.setUserAuthenticateData(userAuthenticateData);

    try {
      Object response = getWebServiceTemplate().marshalSendAndReceive(URI, request, soapActionCallback);
      AuthenticateResponse authenticateResponse = (AuthenticateResponse) response;
      token = authenticateResponse.getToken();
      log.info("Authenticate success");

      return true;
    }
    catch(SoapFaultClientException soapFaultClientException){
        SoapFault soapFault = soapFaultClientException.getSoapFault();
        SoapFaultDetail soapFaultDetail = soapFault.getFaultDetail();
        Iterator<SoapFaultDetailElement> elements = soapFaultDetail.getDetailEntries();
//
//        ServiceFault serviceFault = new ServiceFault();
        while(elements.hasNext()) {
          SoapFaultDetailElement soapFaultDetailElement = elements.next();
          if(soapFaultDetailElement.getName().toString().equals("code")) {
            soapFaultDetailElement.getResult().getSystemId();
          }
          if(soapFaultDetailElement.getName().toString().equals("description")) {
            System.out.println(soapFaultDetailElement.getName().toString());
          }
        }
      }

      log.info("Authenticate failure");
      return false;
    }

  public List<String> getUserAccounts() {
    try {
      log.info("Try get accounts");

      GetUserAccountsRequest request = new GetUserAccountsRequest();
      MyHeaders authorizationHeader = new MyHeaders();
      authorizationHeader.setToken(token);
      Object response = getWebServiceTemplate().marshalSendAndReceive(URI, request, new SecurityHeader(authorizationHeader));

      GetUserAccountsResponse userAccountsResponse = (GetUserAccountsResponse) response;
      log.info("SUCCESS get accounts");

      return userAccountsResponse.getAccounts();
    } catch(SoapFaultClientException soapFaultClientException){
      log.info("Some problem with get all user accounts");
    }

    return null;
  }

  public int getBalance(String selectedAccountNo) throws Exception {
    try {
      log.info("Try get balance of " + selectedAccountNo);

      GetBalanceRequest request = new GetBalanceRequest();
      request.setAccountNo(selectedAccountNo);
      MyHeaders authorizationHeader = new MyHeaders();
      authorizationHeader.setToken(token);
      Object response = getWebServiceTemplate().marshalSendAndReceive(URI, request, new SecurityHeader(authorizationHeader));

      GetBalanceResponse getBalanceResponse = (GetBalanceResponse) response;
      log.info("get balance SUCCESS");

      return getBalanceResponse.getBalance().getBalance();
    } catch(SoapFaultClientException soapFaultClientException){
      log.info("Some problem with get all user accounts");
      throw new Exception("Some problem with get all user accounts");
    }
  }
}
