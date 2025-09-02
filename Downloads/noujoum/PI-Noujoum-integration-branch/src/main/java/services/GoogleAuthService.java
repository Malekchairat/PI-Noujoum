package services;

import java.awt.Desktop;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.net.http.HttpHeaders;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

public class GoogleAuthService {
    private static final String CLIENT_SECRET_JSON = "/client_secret.json"; // Place this file in your resources
    private static final List<String> SCOPES = List.of(
            "https://www.googleapis.com/auth/userinfo.email",
            "https://www.googleapis.com/auth/userinfo.profile"
    );
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public static Userinfo authenticate() throws Exception {
        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        var clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY,
                new InputStreamReader(GoogleAuthService.class.getResourceAsStream(CLIENT_SECRET_JSON))
        );

        var flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .build();

        var receiver = new LocalServerReceiver.Builder().setPort(8889).build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        Oauth2 oauth2 = new Oauth2.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName("Your App Name")
                .build();

        return oauth2.userinfo().get().execute();
    }
}