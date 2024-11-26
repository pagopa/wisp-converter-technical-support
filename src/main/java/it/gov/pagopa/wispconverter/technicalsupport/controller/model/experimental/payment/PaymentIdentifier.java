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
public class PaymentIdentifier {

    @Schema(example = "001924757343397669", description = "The IUV code of the payment")
    @JsonProperty("iuv")
    private String iuv;

    @Schema(example = "012345678901", description = "The identifier of the creditor institution")
    @JsonProperty("domain_id")
    private String domainId;

    @Schema(example = "001924757343397669-00001", description = "The payment context code (aka CCP) of the payment tentative")
    @JsonProperty("ccp")
    private String ccp;

    @Schema(example = "12345678901_aa0518a9-194d-4de2-999f-e462c3746e24", description = "The session ID related to the single payment flow")
    @JsonProperty("session_id")
    private String sessionId;
}
