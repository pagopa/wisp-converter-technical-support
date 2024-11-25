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

    @Schema(description = "")
    @JsonProperty("step")
    private String step;

    @Schema(description = "")
    @JsonProperty("started_at")
    private Instant startedAt;

    @Schema(description = "")
    @JsonProperty("events")
    private List<ReEvent> events;
}
