package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentFlowStatus {

    @Schema(description = "The payment identification information related to the payment analyzed in the flow")
    @JsonProperty("payment_identifier")
    private PaymentIdentifier paymentIdentifier;

    @Schema(description = "The status for each macro-step in the payment flow, defining if something went wrong during process")
    @JsonProperty("payment_details")
    private PaymentFlowDetail paymentDetails;

    @Schema(example = "OK", description = "The final status of the payment flow regarding the correct execution of the entire process")
    @JsonProperty("payment_outcome")
    private String paymentOutcome;

    @Schema(example = "SENT", description = "The final status of the payment flow regarding the correct send of the payment receipt")
    @JsonProperty("receipt_sending_status")
    private String receiptSendingStatus;
}
