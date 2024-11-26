package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentFlowsResponse {

    @Schema(description = "The list of flows executed on certain payment identifier")
    @JsonProperty("flows")
    private List<PaymentFlow> flows;

    @Schema(description = "The metadata related to the extracted content and to the search filters")
    @JsonProperty("metadata")
    private PaymentFlowMetadata metadata;
}
