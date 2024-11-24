package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentFlow {

    @Schema(description = "")
    @JsonProperty("session_id")
    private String sessionId;

    @Schema(description = "")
    @JsonProperty("started_at")
    private Instant startedAt;

    @Schema(description = "")
    @JsonProperty("steps_details")
    private List<PaymentFlowStep> stepsDetails;
}
