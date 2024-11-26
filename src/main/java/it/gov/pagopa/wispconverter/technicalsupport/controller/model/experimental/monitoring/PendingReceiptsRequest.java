package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.monitoring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PendingReceiptsRequest {

    @Schema(example = "2024-01-01T12:00:00", description = "Lower limit date time (in yyyy-MM-ddThh:mm:ss) in 'Europe/Rome' timezone", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty(value = "lower_bound_date", required = true)
    private LocalDateTime lowerBoundDate;

    @Schema(example = "2024-01-01T12:00:00", description = "Upper limit date time (in yyyy-MM-ddThh:mm:ss) in 'Europe/Rome' timezone", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty(value = "upper_bound_date", required = true)
    private LocalDateTime upperBoundDate;

    @Schema(example = "12345678901", description = "The identifier of the creditor institution", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "creditor_institution", required = true)
    private String creditorInstitution;

    @Schema(description = "The list of IUV payment codes to be used as a search filter", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty(value = "iuvs")
    private Set<String> iuvs;
}
