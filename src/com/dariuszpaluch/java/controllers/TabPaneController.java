package com.dariuszpaluch.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.springframework.stereotype.Component;

/**
 * Created by Dariusz Paluch on 07.01.2018.
 */
@Component
public class TabPaneController {
  @FXML public AnchorPane applicationTab;
  @FXML public AnchorPane loggerTab;
}
