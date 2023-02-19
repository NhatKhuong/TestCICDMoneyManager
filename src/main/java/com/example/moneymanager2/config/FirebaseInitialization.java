package com.example.moneymanager2.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.InputStream;

@Service
public class FirebaseInitialization {
    @PostConstruct
    public void initialization() {

        try{
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            FirebaseApp.initializeApp(options);
        }
        catch (Exception error) {
            error.printStackTrace();
        }

    }
}
