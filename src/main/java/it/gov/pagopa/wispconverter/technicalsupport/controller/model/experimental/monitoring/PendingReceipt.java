package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.monitoring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.payment.PaymentIdentifier;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PendingReceipt {

    @JsonProperty("payment")
    private PaymentIdentifier payment;

    @JsonProperty("receipt_to_send")
    private String receiptToSend;

    @JsonProperty("receipt_content")
    private String receiptContent;
}
