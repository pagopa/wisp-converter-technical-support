package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentFlowsFilterRequest {

    @Schema(example = "2024-01-01", description = "The lower limit of the date slot, used as delimiter for the search time (in yyyy-MM-dd)", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "lower_bound_date", required = true)
    private LocalDate lowerBoundDate;

    @Schema(example = "2024-01-01", description = "The upper limit of the date slot, used as delimiter for the search time (in yyyy-MM-dd)", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "lower_bound_date", required = true)
    private LocalDate upperBoundDate;

    @Schema(example = "false", description = "The flag that permits to enable the display of internal states of the entire process", defaultValue = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "show_details", required = true)
    private Boolean showDetails;

    @Schema(example = "true", description = "The flag that permits to enable the display of data in a compact form, reducing to essential information", defaultValue = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "show_compact_form", required = true)
    private Boolean showCompactForm;

    @Schema(example = "false", description = "The flag that permits to enable the display of request and response payloads in INTERFACE events", defaultValue = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "show_payloads", required = true)
    private Boolean showPayloads;
}
