package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.monitoring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.payment.PaymentIdentifier;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PendingReceipt {

    @Schema(description = "The payment identification information related to the receipt to be send if required")
    @JsonProperty("payment")
    private PaymentIdentifier payment;

    @Schema(example = "OK", allowableValues = {"OK", "KO"}, description = "The type of receipt to be send if required")
    @JsonProperty("receipt_to_send")
    private String receiptToSend;

    @Schema(example = "eyJjb250ZW50IjoiPC4uLj4ifQ==", description = "The content of the receipt that can be sent to creditor institution if required")
    @JsonProperty("receipt_content")
    private String receiptContent;
}
