package com.example;


import com.cyberark.conjur.api.Conjur;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.net.ssl.SSLContext;

@Service
public class ConjurService {

    private final Conjur conjur;

    public ConjurService(@Value("${conjur.applianceUrl}") String applianceUrl,
                         @Value("${conjur.account}") String account,
                         @Value("${conjur.authnLogin}") String authnLogin,
                         @Value("${conjur.authnApiKey}") String authnApiKey) {
        System.setProperty("CONJUR_APPLIANCE_URL", applianceUrl);
        System.setProperty("CONJUR_ACCOUNT", account);
        System.setProperty("CONJUR_AUTHN_LOGIN", authnLogin);
        System.setProperty("CONJUR_AUTHN_API_KEY", authnApiKey);

        // Optionally, set SSLContext if required
        SSLContext sslContext = createSSLContext();
        //conjur.setSSLContext(sslContext);

        this.conjur = new Conjur(sslContext);
    }

    public String getDatabasePassword() {
        return conjur.variables().retrieveSecret("secrets/db-password");
    }

    private SSLContext createSSLContext() {
        try {
            // Load the default SSL context (you can customize this if needed)
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SSLContext", e);
        }
    }
}