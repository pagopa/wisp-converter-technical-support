package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentIdentifier {

    @JsonProperty("iuv")
    private String iuv;

    @JsonProperty("domain_id")
    private String domainId;

    @JsonProperty("ccp")
    private String ccp;

    @JsonProperty("session_id")
    private String sessionId;
}
