package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental;

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
public class ReEventExperimentalResponse {

    @Schema(description = "List of flows executed on certain payment position")
    @JsonProperty("flows")
    private List<PaymentFlow> flows;

    @Schema(description = "")
    @JsonProperty("metadata")
    private PaymentFlowMetadata metadata;
}
