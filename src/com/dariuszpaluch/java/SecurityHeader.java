package com.dariuszpaluch.java;

import bank.wsdl.MyHeaders;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by Dariusz Paluch on 10.01.2018.
 */
public class SecurityHeader implements WebServiceMessageCallback {

  private MyHeaders myHeaders;

  public SecurityHeader(MyHeaders myHeaders) {
    this.myHeaders = myHeaders;
  }

  @Override
  public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
    SoapHeader soapHeader = ((SoapMessage)message).getSoapHeader();

    BankClientConfiguration bankClientConfiguration = new BankClientConfiguration();
    Jaxb2Marshaller marshaller = bankClientConfiguration.marshaller();
    marshaller.marshal(myHeaders, soapHeader.getResult());

  }
}