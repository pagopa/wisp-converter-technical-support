package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.payment;

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

    @Schema(example = "12345678901_aa0518a9-194d-4de2-999f-e462c3746e24", description = "The session ID related to a single payment flow")
    @JsonProperty("session_id")
    private String sessionId;

    @Schema(example = "2024-01-01T12:00:00", description = "The date on which the payment flow was triggered")
    @JsonProperty("started_at")
    private Instant startedAt;

    @Schema(description = "The list of each step executed in order to proceed with the payment flow until certain status")
    @JsonProperty("steps_details")
    private List<PaymentFlowStep> stepsDetails;
}
