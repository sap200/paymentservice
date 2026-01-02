package com.mneepay.paymentservice.dto;

import cloud.pangeacyber.pangea.authn.responses.ClientSessionLogoutResponse;
import cloud.pangeacyber.pangea.authn.responses.ClientUserinfoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PangeaResponse {
    private String status;
    private ClientUserinfoResponse userInfo;
    private ClientSessionLogoutResponse logoutResponse;
}
