package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentFlowStatus {

    @JsonProperty("iuv")
    private String iuv;

    @JsonProperty("domain_id")
    private String domainId;

    @JsonProperty("ccp")
    private String ccp;

    @JsonProperty("payment_details")
    private PaymentFlowDetail paymentDetails;

    @JsonProperty("payment_outcome")
    private String paymentOutcome;

    @JsonProperty("receipt_sending_status")
    private String receiptSendingStatus;
}
