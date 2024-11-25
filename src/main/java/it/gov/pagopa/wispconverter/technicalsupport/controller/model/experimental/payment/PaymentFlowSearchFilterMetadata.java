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

    @Schema(description = "")
    @JsonProperty("iuv")
    private String iuv;

    @Schema(description = "")
    @JsonProperty("notice_number")
    private String noticeNumber;

    @Schema(description = "")
    @JsonProperty("session_id")
    private String sessionId;

    @Schema(description = "")
    @JsonProperty("lower_bound_date")
    private LocalDate lowerBoundDate;

    @Schema(description = "")
    @JsonProperty("upper_bound_date")
    private LocalDate upperBoundDate;
}
