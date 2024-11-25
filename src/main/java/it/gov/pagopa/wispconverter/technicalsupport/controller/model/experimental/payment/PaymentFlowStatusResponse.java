package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("lower_bound_date")
    private LocalDate lowerBoundDate;

    @JsonProperty("upper_bound_date")
    private LocalDate upperBoundDate;

    @JsonProperty("payments")
    private List<PaymentFlowStatus> paymentStatuses;
}
