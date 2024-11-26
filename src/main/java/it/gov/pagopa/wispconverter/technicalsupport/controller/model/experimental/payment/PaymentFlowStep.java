package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.ReEvent;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentFlowStep {

    @Schema(example = "redirect", description = "The executed macro-step of the payment flow, containing several micro-steps")
    @JsonProperty("step")
    private String step;

    @Schema(example = "2024-01-01T12:00:00", description = "The date on which the payment flow step is executed")
    @JsonProperty("started_at")
    private Instant startedAt;

    @Schema(description = "The list of micro-steps contained by payment flow step")
    @JsonProperty("events")
    private List<ReEvent> events;
}
