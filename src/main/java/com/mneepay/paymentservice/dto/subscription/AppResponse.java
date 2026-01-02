package com.mneepay.paymentservice.dto.subscription;

import com.mneepay.paymentservice.models.subscription.App;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringEscapeUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppResponse {
    private String status;
    private String errorMessage;
    private App app;
}
