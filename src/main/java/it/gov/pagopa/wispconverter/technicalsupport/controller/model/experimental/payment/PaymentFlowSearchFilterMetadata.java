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
public class PaymentFlowSearchFilterMetadata {

    @Schema(example = "001924757343397669", description = "The IUV code of the payment")
    @JsonProperty("iuv")
    private String iuv;

    @Schema(example = "351147559280022097", description = "The notice number code of the payment")
    @JsonProperty("notice_number")
    private String noticeNumber;

    @Schema(example = "12345678901_aa0518a9-194d-4de2-999f-e462c3746e24", description = "The session ID related to the single payment flow")
    @JsonProperty("session_id")
    private String sessionId;

    @Schema(example = "2024-01-01", description = "The lower limit of the date slot, used as delimiter for the search time (in yyyy-MM-dd)")
    @JsonProperty(value = "lower_bound_date")
    private LocalDate lowerBoundDate;

    @Schema(example = "2024-01-01", description = "The upper limit of the date slot, used as delimiter for the search time (in yyyy-MM-dd)")
    @JsonProperty(value = "upper_bound_date")
    private LocalDate upperBoundDate;
}
