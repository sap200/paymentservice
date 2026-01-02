package com.mneepay.paymentservice.config;

import cloud.pangeacyber.pangea.Config;
import cloud.pangeacyber.pangea.authn.AuthNClient;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PangeaConfig {
    @Value("${pangea.token}")
    private String pangeaToken;

    @Value("${pangea.domain}")
    private String pangeaDomain;

    @Bean
    public AuthNClient authNClient() {
        AuthNClient authNClient = null;
        try {
            Config config = Config.builder().domain(pangeaDomain).token(pangeaToken).build();
            authNClient = new AuthNClient.Builder(config).logger(LogManager.getLogger()).build();
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return authNClient;
    }
}
