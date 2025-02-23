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

          // Load the .pem file from the resources folder
          InputStream pemInputStream = new ClassPathResource("certificate.pem").getInputStream();

          // Create SSLContext using the .pem file
          SSLContext sslContext = createSSLContext(pemInputStream);

        this.conjur = new Conjur(sslContext);
    }

    public String getDatabasePassword() {
        return conjur.variables().retrieveSecret("secrets/db-password");
    }

    private SSLContext createSSLContext(InputStream pemInputStream) throws Exception {
        // Load the certificate from the .pem file
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Certificate certificate = certificateFactory.generateCertificate(pemInputStream);

        // Create a KeyStore and add the certificate
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null); // Initialize an empty KeyStore
        keyStore.setCertificateEntry("conjur-cert", certificate);

        // Initialize TrustManagerFactory with the KeyStore
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        // Create and initialize SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        return sslContext;
    }
}