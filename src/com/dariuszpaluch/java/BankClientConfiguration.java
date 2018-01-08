package com.dariuszpaluch.java;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
/**
 * Created by Dariusz Paluch on 07.01.2018.
 */

public class BankClientConfiguration {
  public Jaxb2Marshaller marshaller() {
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    // this package must match the package in the <generatePackage> specified in
    // pom.xml
    marshaller.setContextPath("bank.wsdl");
    return marshaller;
  }

  public BankClientService bankClientService(Jaxb2Marshaller marshaller) {
    BankClientService client = new BankClientService();
    client.setDefaultUri("http://localhost:8090/ws");
    System.out.println("RUN");
    client.setMarshaller(marshaller);
    client.setUnmarshaller(marshaller);
    return client;
  }
}
