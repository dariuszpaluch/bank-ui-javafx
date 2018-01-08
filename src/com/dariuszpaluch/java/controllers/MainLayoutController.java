package com.dariuszpaluch.java.controllers;

import bank.wsdl.AuthenticateResponse;
import com.dariuszpaluch.java.BankClientService;
import com.dariuszpaluch.java.BankClientConfiguration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;

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

  @FXML
  void initialize() {
    loginButton.setOnAction(this::onClickLoginButton);
  }

  private void onClickLoginButton(ActionEvent actionEvent) {
    String login = this.login.getText();
    String password = this.password.getText();

    BankClientService quoteClient = BankClientService.getInstanceBankClientService();

    Boolean success = quoteClient.tryAuthenticate(login, password);
    if(!success) {
      errorText.setText("Wrong login or password. Try again");
    }
//    this.run();

//    try {
//      Service service = Service.create(new URL("http://localhost:8080/ws/bank.wsdl"), new QName("Bank"));
//      Dispatch<Source> disp = service.createDispatch(new QName("HelloPort"), Source.class, Service.Mode.PAYLOAD);
//      AuthenticateRequest request = new AuthenticateRequest();
//
//      UserAuthenticateData userAuthenticateData = new UserAuthenticateData();
//      userAuthenticateData.setLogin(login);
//      userAuthenticateData.setPassword(password);
//      request.setUserAuthenticateData(userAuthenticateData);
//
//      AuthenticateResponse response = disp.invoke((Source)request);
//    } catch (MalformedURLException e) {
//      e.printStackTrace();
//    }

//    callSoapWebService(soapEndpointUrl, soapAction);
  }

  public String run() {

    return "";
  }
//
//  private static void createSoapEnvelope(SOAPMessage soapMessage) throws SOAPException {
//    SOAPPart soapPart = soapMessage.getSOAPPart();
//
//    String myNamespace = "gs";
//    String myNamespaceURI = "http://spring.io/guides/gs-producing-web-service";
//
//    // SOAP Envelope
//    SOAPEnvelope envelope = soapPart.getEnvelope();
//    envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
//
//            /*
//            Constructed SOAP Request Message:
//            <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:myNamespace="https://www.w3schools.com/xml/">
//                <SOAP-ENV:Header/>
//                <SOAP-ENV:Body>
//                    <myNamespace:CelsiusToFahrenheit>
//                        <myNamespace:Celsius>100</myNamespace:Celsius>
//                    </myNamespace:CelsiusToFahrenheit>
//                </SOAP-ENV:Body>
//            </SOAP-ENV:Envelope>
//            */
//
//    // SOAP Body
//    SOAPBody soapBody = envelope.getBody();
//    SOAPElement soapBodyRoot = soapBody.addChildElement("authenticateRequest", myNamespace);
//    SOAPElement soapBodyElem = soapBodyRoot.addChildElement("userAuthenticateData", myNamespace);
//    SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("login", myNamespace);
//    soapBodyElem1.addTextNode("darek");
//    SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("password", myNamespace);
//    soapBodyElem2.addTextNode("darek123");
//  }
//
//  private static void callSoapWebService(String soapEndpointUrl, String soapAction) {
//    try {
//      // Create SOAP Connection
//      SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
//      SOAPConnection soapConnection = soapConnectionFactory.createConnection();
//
//      // Send SOAP Message to SOAP Server
//      SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction), soapEndpointUrl);
//
//      // Print the SOAP Response
//      System.out.println("Response SOAP Message:");
//      soapResponse.writeTo(System.out);
//      System.out.println();
//
//      soapConnection.close();
//    } catch (Exception e) {
//      System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
//      e.printStackTrace();
//    }
//  }
//
//  private static SOAPMessage createSOAPRequest(String soapAction) throws Exception {
//    MessageFactory messageFactory = MessageFactory.newInstance();
//    SOAPMessage soapMessage = messageFactory.createMessage();
//
//    createSoapEnvelope(soapMessage);
//
////    MimeHeaders headers = soapMessage.getMimeHeaders();
////    headers.addHeader("SOAPAction", soapAction);
//
//    soapMessage.saveChanges();
//
//        /* Print the request message, just for debugging purposes */
//    System.out.println("Request SOAP Message:");
//    soapMessage.writeTo(System.out);
//    System.out.println("\n");
//
//    return soapMessage;
//  }


}
