package com.dariuszpaluch.java;

import bank.wsdl.AuthenticateRequest;
import bank.wsdl.AuthenticateResponse;
import bank.wsdl.ServiceFault;
import bank.wsdl.UserAuthenticateData;
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

/**
 * Created by Dariusz Paluch on 07.01.2018.
 */
public class BankClientService extends WebServiceGatewaySupport {
  private static final Logger log = LoggerFactory.getLogger(BankClientService.class);
  private static BankClientService bankClientService;
  String token;
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
//        javax.xml.soap.SOAPFault fault = soapFaultException.getFault(); //<Fault> node
//
//        ServiceFault serviceFault = (ServiceFault) response;
//        handleServiceFault(serviceFault);
//        log.info("Authenticate failure");
//      }
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
  }
