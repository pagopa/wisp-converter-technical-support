package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentFlowStatusResponse {

    @Schema(example = "2024-01-01", description = "The lower limit of the date slot, used as delimiter for the search time (in yyyy-MM-dd)")
    @JsonProperty(value = "lower_bound_date")
    private LocalDate lowerBoundDate;

    @Schema(example = "2024-01-01", description = "The upper limit of the date slot, used as delimiter for the search time (in yyyy-MM-dd)")
    @JsonProperty(value = "upper_bound_date")
    private LocalDate upperBoundDate;

    @Schema(description = "The status related to each flows found for certain payment identifier")
    @JsonProperty("payments")
    private List<PaymentFlowStatus> paymentStatuses;
}
