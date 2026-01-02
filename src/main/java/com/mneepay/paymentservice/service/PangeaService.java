package com.mneepay.paymentservice.service;

import cloud.pangeacyber.pangea.authn.AuthNClient;
import cloud.pangeacyber.pangea.authn.responses.ClientSessionLogoutResponse;
import cloud.pangeacyber.pangea.authn.responses.ClientUserinfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PangeaService {
    @Autowired
    private AuthNClient authNClient;

    public PangeaService(AuthNClient authNClient) {
        this.authNClient = authNClient;
    }

    public ClientUserinfoResponse getClientUserInfo(String code) throws Exception {
        return authNClient.client().userinfo(code);
    }

    public ClientSessionLogoutResponse sessionLogout(String token) throws  Exception {
        return authNClient.client().session().logout(token);
    }
}
