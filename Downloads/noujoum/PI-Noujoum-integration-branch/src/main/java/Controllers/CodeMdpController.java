/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;


public class CodeMdpController implements Initializable {

    @FXML
    private TextField code;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }


    public void setCode(int c){

        code.setText(String.valueOf(c));

    }








}