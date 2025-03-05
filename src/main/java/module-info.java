module org.example.pirec {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires com.google.gson;
    requires java.desktop;
    requires javafx.swing;
    requires kernel;
    requires layout;
    requires io;
    requires java.mail;
    requires mysql.connector.j;
    requires twilio;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    opens controller to javafx.fxml;
    opens models to javafx.fxml;
    opens services to javafx.fxml;
    opens tools to javafx.fxml;
    opens org.example to javafx.graphics, javafx.fxml;

    exports controller;
    exports models;
    exports services;
    exports tools;
    exports org.example;

}